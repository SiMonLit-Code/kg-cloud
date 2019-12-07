package com.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.kg.api.edit.ConceptEntityApi;
import ai.plantdata.kg.api.pub.EntityApi;
import ai.plantdata.kg.api.pub.RelationApi;
import ai.plantdata.kg.api.pub.SchemaApi;
import ai.plantdata.kg.api.pub.req.AggRelationFrom;
import ai.plantdata.kg.api.pub.req.SearchByAttributeFrom;
import ai.plantdata.kg.api.pub.resp.EntityVO;
import ai.plantdata.kg.api.pub.resp.PromptItemVO;
import ai.plantdata.kg.api.semantic.QuestionAnswersApi;
import ai.plantdata.kg.api.semantic.req.NerSearchReq;
import ai.plantdata.kg.api.semantic.rsp.SemanticSegWordVO;
import ai.plantdata.kg.common.bean.BasicInfo;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.config.EsConfig;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.constant.PromptQaTypeEnum;
import com.plantdata.kgcloud.domain.app.converter.ConditionConverter;
import com.plantdata.kgcloud.domain.app.converter.EntityConverter;
import com.plantdata.kgcloud.domain.app.converter.PromptConverter;
import com.plantdata.kgcloud.domain.app.converter.RelationConverter;
import com.plantdata.kgcloud.domain.app.service.GraphPromptService;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.domain.app.util.PageUtils;
import com.plantdata.kgcloud.domain.dataset.constant.DataType;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptConnect;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProvider;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProviderFactory;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfQaService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.constant.AttrDefinitionTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.EdgeAttrPromptReq;
import com.plantdata.kgcloud.sdk.req.app.PromptReq;
import com.plantdata.kgcloud.sdk.req.app.PromptSearchInterface;
import com.plantdata.kgcloud.sdk.req.app.SeniorPromptReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfQaRsp;
import com.plantdata.kgcloud.sdk.rsp.app.EdgeAttributeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SeniorPromptRsp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/22 15:37
 */
@Service
@Slf4j
public class GraphPromptServiceImpl implements GraphPromptService {

    @Autowired
    private EntityApi entityApi;
    @Autowired
    private SchemaApi schemaApi;
    @Autowired
    private RelationApi relationApi;
    @Autowired
    private QuestionAnswersApi questionAnswersApi;
    @Autowired
    private GraphConfQaService graphConfQaService;
    @Autowired
    private ConceptEntityApi conceptEntityApi;

    @Override
    public List<PromptEntityRsp> prompt(String kgName, PromptReq promptReq) {
        PromptQaTypeEnum qaType = PromptQaTypeEnum.parseWitDefault(promptReq.getPromptType());
        if (PromptQaTypeEnum.BEFORE.equals(qaType)) {
            return queryAnswer(kgName, promptReq);
        }
        if (promptReq.getOpenExportDate()) {
            //执行es搜索
            List<PromptEntityRsp> entityRspList = queryFromEs(kgName, promptReq);
            if (!CollectionUtils.isEmpty(entityRspList)) {
                return entityRspList;
            }
        }

        Optional<List<PromptItemVO>> promptOpt = RestRespConverter.convert(entityApi.promptList(kgName, PromptConverter.reqToRemote(promptReq)));

        List<PromptEntityRsp> entityRspList = PromptConverter.voToRsp(promptOpt.orElse(Lists.newArrayList()));

        if (PromptQaTypeEnum.END.equals(qaType)) {
            entityRspList.addAll(queryAnswer(kgName, promptReq));
        }
        return entityRspList;
    }

    @Override
    public List<SeniorPromptRsp> seniorPrompt(String kgName, SeniorPromptReq seniorPromptReq) {
        if (seniorPromptReq.getOpenExportDate() && !StringUtils.isEmpty(seniorPromptReq.getKw())) {
            List<PromptEntityRsp> entityRspList = queryFromEs(kgName, seniorPromptReq);
            if (!CollectionUtils.isEmpty(entityRspList)) {
                return PromptConverter.rspToBelow(entityRspList);
            }
        }
        Set<Long> entityIds = queryEntityIdsByAttr(kgName, seniorPromptReq);
        Optional<List<EntityVO>> entityOpt = RestRespConverter.convert(entityApi.serviceEntity(kgName, EntityConverter.buildIdsQuery(entityIds)));
        if (!entityOpt.isPresent() || CollectionUtils.isEmpty(entityOpt.get())) {
            return Collections.emptyList();
        }
        return PromptConverter.voToSeniorRsp(entityOpt.get());
    }

    @Override
    public List<EdgeAttributeRsp> edgeAttributeSearch(String kgName, EdgeAttrPromptReq promptReq) {
        Optional<List<Map<Object, Integer>>> aggOpt;
        if (AttrDefinitionTypeEnum.OBJECT.equals(promptReq.getDataType())) {
            AggRelationFrom relationFrom = RelationConverter.edgeAttrPromptReqToAggRelationFrom(promptReq);
            aggOpt = RestRespConverter.convert(relationApi.aggRelation(kgName, relationFrom));

        } else if (AttrDefinitionTypeEnum.DATA_VALUE.equals(promptReq.getDataType())) {
            aggOpt = RestRespConverter.convert(schemaApi.aggAttr(kgName, PromptConverter.edgeAttrPromptReqToAggAttrValueFrom(promptReq)));
        } else {
            log.error("dataType:{}", promptReq.getDataType());
            throw new BizException(KgmsErrorCodeEnum.ATTRIBUTE_DEFINITION_NOT_EXISTS);
        }
        if (!aggOpt.isPresent() || CollectionUtils.isEmpty(aggOpt.get())) {
            return Collections.emptyList();
        }
        return RelationConverter.mapToEdgeAttributeRsp(aggOpt.get());
    }

    private List<PromptEntityRsp> queryFromEs(String kgName, PromptSearchInterface promptReq) {
        DataOptConnect connect = DataOptConnect.builder()
                .addresses(EsConfig.getAddress())
                .database(kgName)
                .build();
        DataOptProvider provider = DataOptProviderFactory.createProvider(connect, DataType.ELASTIC);
        List<Map<String, Object>> maps = provider.find(promptReq.getOffset(), promptReq.getLimit(), PromptConverter.buildEsParam(promptReq));
        if (maps == null || maps.isEmpty()) {
            return Collections.emptyList();
        }
        return PromptConverter.esResultToEntity(maps);

    }

    private List<PromptEntityRsp> queryAnswer(String kgName, PromptReq promptReq) {

        List<PromptEntityRsp> rs = Lists.newArrayList();
        NerSearchReq nerParam = new NerSearchReq();
        nerParam.setKgName(kgName);
        nerParam.setQuery(promptReq.getKw());

        List<SemanticSegWordVO> ner = RestRespConverter.convert(questionAnswersApi.ner(nerParam)).orElse(Collections.emptyList());
        if (ner.isEmpty() || ner.size() > 3) {
            return rs;
        }
        List<GraphConfQaRsp> qaTemplates = graphConfQaService.findByKgName(kgName);

        qaTemplates.sort(Comparator.comparing(GraphConfQaRsp::getPriority).reversed());

        if (qaTemplates.isEmpty()) {
            return rs;
        }
        for (GraphConfQaRsp qaTemplate : qaTemplates) {
            if (ner.size() == qaTemplate.getCount()) {
                String s = buildQuestion(kgName, ner, qaTemplate);
                if (s == null) {
                    continue;
                }
                PromptEntityRsp entityBean = new PromptEntityRsp();
                entityBean.setName(s);
                entityBean.setQa(true);
                rs.add(entityBean);
            }
        }
        //取一个实体和一个实体的模板
        if (rs.isEmpty() && ner.size() > 1) {
            for (SemanticSegWordVO nerResult : ner) {
                for (GraphConfQaRsp template : qaTemplates) {
                    if (rs.size() == 2) {
                        break;
                    }
                    if (template.getCount() == 1) {
                        String s = buildQuestion(kgName, Lists.newArrayList(nerResult), template);
                        if (s == null) {
                            continue;
                        }
                        PromptEntityRsp entityBean = new PromptEntityRsp();
                        entityBean.setName(s);
                        entityBean.setQa(true);
                        rs.add(entityBean);
                    }
                }
            }
        }
        return rs.stream().filter(e -> e.getName().toLowerCase().startsWith(promptReq.getKw().toLowerCase())).collect(Collectors.toList());
    }

    private String buildQuestion(String kgName, List<SemanticSegWordVO> entities, GraphConfQaRsp template) {
        String question = template.getQuestion();
        for (int i = 0; i < template.getCount(); i++) {
            SemanticSegWordVO nerResult = entities.get(i);
            if (!checkConcept(JsonUtils.readToList(template.getConceptIds(), Long.class), nerResult.getEntityClassIdList(), kgName)) {
                question = null;
                break;
            }
            if (question != null) {
                question = question.replaceFirst("\\$entity", nerResult.getWord());
            }
        }
        return question;
    }

    private boolean checkConcept(List<Long> source, List<Long> target, String kgName) {
        List<Long> allSource = Lists.newArrayList(source);

        for (Long aLong : source) {
            List<BasicInfo> son = RestRespConverter.convert(conceptEntityApi.tree(kgName, aLong)).orElse(Collections.emptyList());
            if (son.isEmpty()) {
                continue;
            }
            allSource.addAll(son.stream().filter(t -> !t.getId().equals(aLong)).map(BasicInfo::getId).collect(Collectors.toList()));
        }
        for (Long s : allSource) {
            for (Long t : target) {
                if (s.equals(t)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Set<Long> queryEntityIdsByAttr(String kgName, SeniorPromptReq seniorPromptReq) {
        List<Map<String, Object>> queryMapList = ConditionConverter.entityScreeningListToMap(seniorPromptReq.getQuery());
        SearchByAttributeFrom attributeFrom = new SearchByAttributeFrom();
        attributeFrom.setKvMap(queryMapList.isEmpty() ? null : queryMapList.get(0));
        attributeFrom.setEntityName(seniorPromptReq.getKw());
        attributeFrom.setConceptIds(Lists.newArrayList(seniorPromptReq.getConceptId()));
        List<EntityVO> queryList;
        if (queryMapList.size() < 2) {
            attributeFrom.setSkip(seniorPromptReq.getPage());
            attributeFrom.setLimit(seniorPromptReq.getSize());
            queryList = RestRespConverter.convert(entityApi.searchByAttribute(kgName, attributeFrom)).orElse(Collections.emptyList());

        } else {
            attributeFrom.setSkip(NumberUtils.INTEGER_ZERO);
            attributeFrom.setLimit(Integer.MAX_VALUE);
            queryList = queryMapList.stream().flatMap(s -> RestRespConverter.convert(entityApi.searchByAttribute(kgName, attributeFrom)).orElse(Collections.emptyList()).stream()).collect(Collectors.toList());
            queryList = PageUtils.subList(seniorPromptReq.getPage(), seniorPromptReq.getSize(), queryList);
        }
        return CollectionUtils.isEmpty(queryList) ? Collections.emptySet() : queryList.stream().map(EntityVO::getId).collect(Collectors.toSet());
    }
}
