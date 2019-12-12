package com.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.kg.api.edit.AttributeApi;
import ai.plantdata.kg.api.edit.ConceptEntityApi;
import ai.plantdata.kg.api.edit.GraphApi;
import ai.plantdata.kg.api.edit.resp.AttrDefVO;
import ai.plantdata.kg.api.edit.resp.SchemaVO;
import ai.plantdata.kg.api.pub.EntityApi;
import ai.plantdata.kg.api.pub.req.KgServiceEntityFrom;
import ai.plantdata.kg.api.pub.resp.EntityVO;
import ai.plantdata.kg.common.bean.AttributeDefinition;
import ai.plantdata.kg.common.bean.BasicInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.domain.app.converter.AttrDefGroupConverter;
import com.plantdata.kgcloud.domain.app.converter.EntityConverter;
import com.plantdata.kgcloud.domain.app.converter.KnowledgeRecommendConverter;
import com.plantdata.kgcloud.domain.app.service.GraphHelperService;
import com.plantdata.kgcloud.domain.app.util.DefaultUtils;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.domain.edit.service.ConceptService;
import com.plantdata.kgcloud.sdk.rsp.app.main.AttributeDefinitionGroupRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfFocus;
import com.plantdata.kgcloud.domain.graph.config.repository.GraphConfFocusRepository;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.constant.GraphInitBaseEnum;
import com.plantdata.kgcloud.sdk.req.app.GraphInitRsp;
import com.plantdata.kgcloud.sdk.req.app.InfoBoxReq;
import com.plantdata.kgcloud.sdk.rsp.app.main.BasicConceptTreeRsp;
import com.plantdata.kgcloud.domain.app.service.GraphApplicationService;
import com.plantdata.kgcloud.domain.app.converter.AttrDefConverter;
import com.plantdata.kgcloud.domain.app.converter.ConceptConverter;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.graph.attr.dto.AttrDefGroupDTO;
import com.plantdata.kgcloud.domain.graph.attr.service.GraphAttrGroupService;
import com.plantdata.kgcloud.sdk.req.app.KnowledgeRecommendReq;
import com.plantdata.kgcloud.sdk.req.app.ObjectAttributeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 14:37
 */
@Service
public class GraphApplicationServiceImpl implements GraphApplicationService {

    @Autowired
    private ai.plantdata.kg.api.pub.GraphApi editGraphApi;
    @Autowired
    private GraphApi graphApi;
    @Autowired
    private GraphAttrGroupService graphAttrGroupService;
    @Autowired
    private EntityApi entityApi;
    @Autowired
    private ConceptEntityApi conceptEntityApi;
    @Autowired
    private AttributeApi attributeApi;
    @Autowired
    private GraphConfFocusRepository graphConfFocusRepository;
    @Autowired
    private GraphHelperService graphHelperService;
    @Autowired
    private ConceptService conceptService;

    @Override
    public SchemaRsp querySchema(String kgName) {
        SchemaRsp schemaRsp = new SchemaRsp();
        Optional<SchemaVO> schemaOptional = RestRespConverter.convert(graphApi.schema(kgName));
        if (!schemaOptional.isPresent()) {
            return schemaRsp;
        }
        SchemaVO schemaVO = schemaOptional.get();
        if (!CollectionUtils.isEmpty(schemaVO.getAttrs())) {
            schemaRsp.setAttrs(AttrDefConverter.voToRsp(schemaVO.getAttrs()));
        }
        if (!CollectionUtils.isEmpty(schemaVO.getConcepts())) {
            schemaRsp.setKgTitle(ConceptConverter.getKgTittle(schemaVO.getConcepts()));
            schemaRsp.setTypes(ConceptConverter.voToRsp(schemaVO.getConcepts()));
        }
        List<AttrDefGroupDTO> attrDefGroupList = graphAttrGroupService.queryAllByKgName(kgName);
        schemaRsp.setAttrGroups(DefaultUtils.getIfNoNull(attrDefGroupList, AttrDefGroupConverter::dtoToRsp));
        return schemaRsp;
    }

    @Override
    public List<ObjectAttributeRsp> knowledgeRecommend(String kgName, KnowledgeRecommendReq knowledgeRecommendReq) {
        Optional<Map<Integer, Set<Long>>> entityAttrOpt = RestRespConverter.convert(entityApi.entityAttributesObject(kgName, KnowledgeRecommendConverter.reqToFrom(knowledgeRecommendReq)));
        if (!entityAttrOpt.isPresent()) {
            return Collections.emptyList();
        }
        List<Long> entityIdList = entityAttrOpt.get().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(entityIdList)) {
            return Collections.emptyList();
        }
        Optional<List<EntityVO>> entityOpt = RestRespConverter.convert(entityApi.serviceEntity(kgName, EntityConverter.buildIdsQuery(entityIdList)));

        return KnowledgeRecommendConverter.voToRsp(entityAttrOpt.get(), entityOpt.orElse(Collections.emptyList()));
    }

    @Override
    public BasicConceptTreeRsp visualModels(String kgName, boolean display, Long conceptId) {
        Optional<List<BasicInfo>> conceptOpt = RestRespConverter.convert(conceptEntityApi.tree(kgName, conceptId));
        BasicConceptTreeRsp conceptTreeRsp = new BasicConceptTreeRsp(NumberUtils.LONG_ZERO, kgName);
        if (!conceptOpt.isPresent()) {
            return conceptTreeRsp;
        }
        if (!display) {
            return ConceptConverter.voToConceptTree(conceptOpt.get(), conceptTreeRsp);
        }
        Optional<List<AttrDefVO>> attrDefOpt = RestRespConverter.convert(attributeApi.getByConceptIds(kgName, AttrDefConverter.convertToQuery(Lists.newArrayList(conceptId), true, 0)));
        return ConceptConverter.voToConceptTree(conceptOpt.get(), attrDefOpt.orElse(Collections.emptyList()), conceptTreeRsp);
    }

    @Override
    public GraphInitRsp initGraphExploration(String kgName, GraphInitBaseEnum graphInitType) throws JsonProcessingException {
        Optional<GraphConfFocus> focusOpt = graphConfFocusRepository.findByKgNameAndType(kgName, graphInitType.getValue());
        GraphInitRsp graphInitRsp = new GraphInitRsp();
        if (focusOpt.isPresent()) {
            GraphConfFocus initGraphBean = focusOpt.get();
            if (initGraphBean.getEntities() != null && initGraphBean.getEntities().fieldNames().hasNext()) {
                graphInitRsp.setEntities(JsonUtils.readToList(initGraphBean.getEntities(), GraphInitRsp.GraphInitEntityRsp.class));
                return graphInitRsp;
            }
        }
        Optional<List<Long>> entityIdOpt = RestRespConverter.convert(editGraphApi.getRelationEntity(kgName));
        if (!entityIdOpt.isPresent()) {
            return graphInitRsp;
        }
        Optional<List<EntityVO>> entityOpt = RestRespConverter.convert(entityApi.serviceEntity(kgName, EntityConverter.buildIdsQuery(entityIdOpt.get())));
        if (!entityOpt.isPresent()) {
            return graphInitRsp;
        }
        graphInitRsp.setEntities(EntityConverter.entityVoToGraphInitEntityRsp(entityOpt.get()));
        return graphInitRsp;
    }

    @Override
    public List<BasicInfoVO> conceptTree(String kgName, Long conceptId, String conceptKey) {
        if (null == conceptId && null == conceptKey) {
            throw BizException.of(AppErrorCodeEnum.NULL_CONCEPT_ID_AND_KEY);
        }
        if (null == conceptId && StringUtils.isNotEmpty(conceptKey)) {
            List<Long> longs = graphHelperService.replaceByConceptKey(kgName, Lists.newArrayList(conceptKey));
            conceptId = CollectionUtils.isEmpty(longs) ? NumberUtils.LONG_ZERO : longs.get(0);
        }
        if (conceptId == null) {
            conceptId = NumberUtils.LONG_ZERO;
        }

        return conceptService.getConceptTree(kgName, conceptId);
    }

    @Override
    public List<InfoBoxRsp> infoBox(String kgName, InfoBoxReq req) {
        KgServiceEntityFrom entityFrom = new KgServiceEntityFrom();
        entityFrom.setIds(req.getEntityIdList());
        entityFrom.setReadObjectAttribute(req.getRelationAttrs());
        entityFrom.setReadMetaData(false);
        entityFrom.setReadReverseObjectAttribute(req.getReverseRelationAttrs());
        if (!CollectionUtils.isEmpty(req.getAllowAttrs())) {
            entityFrom.setAllowAtts(req.getAllowAttrs());
        } else if (!CollectionUtils.isEmpty(req.getAllowAttrsKey())) {
            entityFrom.setAllowAtts(graphHelperService.replaceByAttrKey(kgName, req.getAllowAttrsKey()));
        }
        Optional<List<EntityVO>> entityOpt = RestRespConverter.convert(entityApi.serviceEntity(kgName, entityFrom));

        if (!entityOpt.isPresent()) {
            return Collections.emptyList();
        }
        Set<Long> relationEntityIdSet = Sets.newHashSet();
        Set<Integer> attrDefIdSet = Sets.newHashSet();
        entityOpt.get().forEach(a -> {
            if (!CollectionUtils.isEmpty(a.getObjectAttributes())) {
                a.getObjectAttributes().forEach((k, v) -> {
                    attrDefIdSet.add(Integer.parseInt(k));
                    relationEntityIdSet.addAll(v);
                });
            }
            if (!CollectionUtils.isEmpty(a.getReverseObjectAttributes())) {
                a.getReverseObjectAttributes().forEach((k, v) -> {
                    attrDefIdSet.add(Integer.parseInt(k));
                    relationEntityIdSet.addAll(v);
                });
            }
            if (!CollectionUtils.isEmpty(a.getDataAttributes())) {
                attrDefIdSet.addAll(a.getDataAttributes().keySet().stream().map(Integer::valueOf).collect(Collectors.toList()));
            }
        });
        Optional<List<AttributeDefinition>> attrDefOpt = RestRespConverter.convert(attributeApi.listByIds(kgName, Lists.newArrayList(attrDefIdSet)));
        Optional<List<ai.plantdata.kg.api.edit.resp.EntityVO>> relationEntityOpt = RestRespConverter.convert(conceptEntityApi.listByIds(kgName, true, Lists.newArrayList(relationEntityIdSet)));

        return EntityConverter.voToInfoBox(entityOpt.get(), attrDefOpt.orElse(Collections.emptyList()), relationEntityOpt.orElse(Collections.emptyList()));
    }
}
