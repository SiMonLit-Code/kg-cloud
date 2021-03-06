package ai.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.kg.api.pub.EntityApi;
import ai.plantdata.kg.api.pub.GraphApi;
import ai.plantdata.kg.api.pub.RelationApi;
import ai.plantdata.kg.api.pub.SchemaApi;
import ai.plantdata.kg.api.pub.req.AggRelationFrom;
import ai.plantdata.kg.api.pub.req.SearchByAttributeFrom;
import ai.plantdata.kg.api.pub.resp.EntityVO;
import ai.plantdata.kg.api.pub.resp.PromptItemVO;
import ai.plantdata.kg.api.semantic.QuestionAnswersApi;
import ai.plantdata.kg.api.semantic.req.NerSearchReq;
import ai.plantdata.kg.support.SegmentWordVO;
import ai.plantdata.kgcloud.constant.AppConstants;
import ai.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import ai.plantdata.kgcloud.constant.PromptQaTypeEnum;
import ai.plantdata.kgcloud.domain.app.converter.BasicConverter;
import ai.plantdata.kgcloud.domain.app.converter.ConditionConverter;
import ai.plantdata.kgcloud.domain.app.converter.PromptConverter;
import ai.plantdata.kgcloud.domain.app.converter.RelationConverter;
import ai.plantdata.kgcloud.domain.app.service.GraphHelperService;
import ai.plantdata.kgcloud.domain.app.service.GraphPromptService;
import ai.plantdata.kgcloud.domain.app.util.PageUtils;
import ai.plantdata.kgcloud.domain.common.util.EnumUtils;
import ai.plantdata.kgcloud.domain.common.util.KGUtil;
import ai.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import ai.plantdata.kgcloud.domain.graph.config.service.GraphConfQaService;
import ai.plantdata.kgcloud.sdk.constant.EdgeAttrPromptDataTypeEnum;
import ai.plantdata.kgcloud.sdk.req.app.EdgeAttrPromptReq;
import ai.plantdata.kgcloud.sdk.req.app.PromptReq;
import ai.plantdata.kgcloud.sdk.req.app.SeniorPromptReq;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfQaRsp;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfQaStatusRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.EdgeAttributeRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.main.SeniorPromptRsp;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private GraphApi graphApi;
    @Autowired
    private GraphHelperService graphHelperService;

    @Override
    public List<PromptEntityRsp> prompt(String kgName, PromptReq promptReq) {
        graphHelperService.replaceByConceptKey(kgName, promptReq);
        // 获取问答状态
        GraphConfQaStatusRsp graphConfQaStatusRsp = graphConfQaService.getStatus(kgName);
        PromptQaTypeEnum qaType = PromptQaTypeEnum.parseWitDefault(promptReq.getPromptType());

        RestResp<List<PromptItemVO>> promptList = entityApi.promptList(KGUtil.dbName(kgName), PromptConverter.promptReqReqToPromptListFrom(promptReq));
        if (PromptQaTypeEnum.BEFORE.equals(qaType) && graphConfQaStatusRsp != null && graphConfQaStatusRsp.getStatus() == 1) {
            List<PromptEntityRsp> entityRspList = BasicConverter.listConvert(RestRespConverter.convert(promptList).orElse(Collections.emptyList()), PromptConverter::promptItemVoToPromptEntityRsp);
            // 是否返回顶层概念
            if (!promptReq.getIsReturnTop()) {
                entityRspList = entityRspList.stream().filter(s -> s.getId() != 0).collect(Collectors.toList());
            }
            return BasicConverter.mergeList(queryAnswer(kgName, promptReq), entityRspList);
        }

        List<PromptEntityRsp> entityRspList = BasicConverter.listConvert(RestRespConverter.convert(promptList).orElse(Collections.emptyList()), PromptConverter::promptItemVoToPromptEntityRsp);
        // 是否返回顶层概念
        if (!promptReq.getIsReturnTop()) {
            entityRspList = entityRspList.stream().filter(s -> s.getId() != 0).collect(Collectors.toList());
        }

        if (PromptQaTypeEnum.END.equals(qaType) && graphConfQaStatusRsp != null && graphConfQaStatusRsp.getStatus() == 1) {
            return BasicConverter.mergeList(entityRspList, queryAnswer(kgName, promptReq));
        }
        if (promptReq.getSort() == -1) {
            Collections.reverse(entityRspList);
        }
        return entityRspList;
    }

    @Override
    public List<SeniorPromptRsp> seniorPrompt(String kgName, SeniorPromptReq seniorPromptReq) {
        List<EntityVO> entityList = queryEntityIdsByAttr(kgName, seniorPromptReq);
        List<SeniorPromptRsp> result = BasicConverter.listConvert(entityList, PromptConverter::entityVoToSeniorPromptRsp);
        List<SeniorPromptRsp> resultWithoutConcept = new ArrayList<>();
        for (SeniorPromptRsp rsp : result) {
            if (rsp.getConceptId() != null && rsp.getConceptId() != 0) {
                resultWithoutConcept.add(rsp);
            }
        }
        return resultWithoutConcept;
    }


    @Override
    public List<EdgeAttributeRsp> edgeAttributeSearch(String kgName, EdgeAttrPromptReq promptReq) {
        Optional<List<Map<Object, Integer>>> aggOpt;
        Optional<EdgeAttrPromptDataTypeEnum> enumObject = EnumUtils.parseById(EdgeAttrPromptDataTypeEnum.class, promptReq.getDataType());
        EdgeAttrPromptDataTypeEnum dataType = enumObject.orElse(EdgeAttrPromptDataTypeEnum.EDGE_ATTR);
        if (EdgeAttrPromptDataTypeEnum.EDGE_ATTR.equals(dataType)) {
            AggRelationFrom relationFrom = RelationConverter.edgeAttrPromptReqToAggRelationFrom(promptReq);
            aggOpt = RestRespConverter.convert(relationApi.aggRelation(KGUtil.dbName(kgName), relationFrom));
        } else if (EdgeAttrPromptDataTypeEnum.NUM_ATTR.equals(dataType)) {
            aggOpt = RestRespConverter.convert(schemaApi.aggAttr(KGUtil.dbName(kgName), PromptConverter.edgeAttrPromptReqToAggAttrValueFrom(promptReq)));
        } else {
            log.error("dataType:{}", promptReq.getDataType());
            throw new BizException(KgmsErrorCodeEnum.ATTRIBUTE_DEFINITION_NOT_EXISTS);
        }
        if (!aggOpt.isPresent() || CollectionUtils.isEmpty(aggOpt.get())) {
            return Collections.emptyList();
        }
        return RelationConverter.mapToEdgeAttributeRsp(aggOpt.get());
    }

    private List<PromptEntityRsp> queryAnswer(String kgName, PromptReq promptReq) {

        List<PromptEntityRsp> rs = Lists.newArrayList();
        NerSearchReq nerParam = new NerSearchReq();
        nerParam.setKgName(KGUtil.dbName(kgName));
        nerParam.setQuery(promptReq.getKw());
        List<SegmentWordVO> ner = RestRespConverter.convert(questionAnswersApi.ner(nerParam)).orElse(Collections.emptyList());

        if (ner.isEmpty() || ner.size() > AppConstants.NER_NUMBER) {
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
            for (SegmentWordVO nerResult : ner) {
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

    private String buildQuestion(String kgName, List<SegmentWordVO> entities, GraphConfQaRsp template) {
        String question = template.getQuestion();
        for (int i = 0; i < template.getCount(); i++) {
            SegmentWordVO nerResult = entities.get(i);
            List<Long> conceptIds = JacksonUtils.readValue(JacksonUtils.writeValueAsString(template.getConceptIds()), new TypeReference<List<Long>>() {
            });
            if (!checkConcept(conceptIds, nerResult.getEntityClassIdList(), kgName)) {
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
        Map<Long, List<Long>> sonOptMap = RestRespConverter.convert(graphApi.sons(KGUtil.dbName(kgName), source)).orElse(Collections.emptyMap());
        List<Long> allSource = Lists.newArrayList(source);
        List<Long> son = source.stream()
                .filter(sonOptMap::containsKey)
                .map(conceptId -> sonOptMap.get(conceptId).stream().filter(t -> !t.equals(conceptId)).collect(Collectors.toList())
                ).flatMap(Collection::stream).collect(Collectors.toList());
        BasicConverter.consumerIfNoNull(son, allSource::addAll);
        return allSource.stream().anyMatch(target::contains);
    }

    private List<EntityVO> queryEntityIdsByAttr(String kgName, SeniorPromptReq seniorPromptReq) {
        List<Map<String, Object>> queryMapList = ConditionConverter.entityScreeningListToMap(seniorPromptReq.getQuery());
        SearchByAttributeFrom attributeFrom = PromptConverter.seniorPromptReqToSearchByAttributeFrom(seniorPromptReq, queryMapList);
        List<EntityVO> queryList;
        if (queryMapList.size() < AppConstants.NER_ENTITY_NUMBER) {
            attributeFrom.setSkip(seniorPromptReq.getOffset());
            attributeFrom.setLimit(seniorPromptReq.getLimit());
            return RestRespConverter.convert(entityApi.searchByAttribute(KGUtil.dbName(kgName), attributeFrom)).orElse(Collections.emptyList());
        }
        attributeFrom.setSkip(NumberUtils.INTEGER_ZERO);
        attributeFrom.setLimit(Integer.MAX_VALUE);
        queryList = queryMapList.stream().flatMap(s -> RestRespConverter.convert(entityApi.searchByAttribute(KGUtil.dbName(kgName), attributeFrom)).orElse(Collections.emptyList()).stream()).collect(Collectors.toList());
        return PageUtils.subList(seniorPromptReq.getPage(), seniorPromptReq.getSize(), queryList);
    }
}
