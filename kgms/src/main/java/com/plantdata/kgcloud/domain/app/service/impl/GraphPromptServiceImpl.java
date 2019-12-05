package com.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.kg.api.pub.EntityApi;
import ai.plantdata.kg.api.pub.RelationApi;
import ai.plantdata.kg.api.pub.req.AggRelationFrom;
import ai.plantdata.kg.api.pub.req.SearchByAttributeFrom;
import ai.plantdata.kg.api.pub.resp.EntityVO;
import ai.plantdata.kg.api.pub.resp.PromptItemVO;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.constant.PromptQaTypeEnum;
import com.plantdata.kgcloud.domain.app.converter.ConditionConverter;
import com.plantdata.kgcloud.domain.app.converter.EntityConverter;
import com.plantdata.kgcloud.domain.app.converter.PromptConverter;
import com.plantdata.kgcloud.domain.app.converter.RelationConverter;
import com.plantdata.kgcloud.domain.app.service.GraphPromptService;
import com.plantdata.kgcloud.domain.app.util.PageUtils;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.sdk.constant.AttrDefinitionTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.EdgeAttrPromptReq;
import com.plantdata.kgcloud.sdk.req.app.PromptReq;
import com.plantdata.kgcloud.sdk.req.app.SeniorPromptReq;
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
    private RelationApi relationApi;

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
        if (AttrDefinitionTypeEnum.OBJECT.equals(promptReq.getDataType())) {
            AggRelationFrom relationFrom = RelationConverter.edgeAttrPromptReqToAggRelationFrom(promptReq);
            Optional<List<Map<Object, Integer>>> aggOpt = RestRespConverter.convert(relationApi.aggRelation(kgName, relationFrom));
            if (!aggOpt.isPresent() || CollectionUtils.isEmpty(aggOpt.get())) {
                return Collections.emptyList();
            }
            return RelationConverter.mapToEdgeAttributeRsp(aggOpt.get());
        }
        //底层搜索数值属性 todo
        return null;
    }

    private List<PromptEntityRsp> queryFromEs(String kgName, Object promptReq) {
        //es todo
        return Collections.emptyList();
    }

    private List<PromptEntityRsp> queryAnswer(String kgName, PromptReq promptReq) {
        //问答 todo
        return Collections.emptyList();
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
