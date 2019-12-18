package com.plantdata.kgcloud.domain.edit.service.impl;

import ai.plantdata.kg.api.edit.AttributeApi;
import ai.plantdata.kg.api.edit.BatchApi;
import ai.plantdata.kg.api.edit.ConceptEntityApi;
import ai.plantdata.kg.api.edit.GraphApi;
import ai.plantdata.kg.api.edit.req.AttrQueryFrom;
import ai.plantdata.kg.api.edit.req.AttributeConstraints;
import ai.plantdata.kg.api.edit.req.AttributeDefinitionFrom;
import ai.plantdata.kg.api.edit.req.BatchQueryRelationFrom;
import ai.plantdata.kg.api.edit.req.DeleteRelationFrom;
import ai.plantdata.kg.api.edit.req.EdgeFrom;
import ai.plantdata.kg.api.edit.req.UpdateRelationFrom;
import ai.plantdata.kg.api.edit.resp.AttrConstraintsVO;
import ai.plantdata.kg.api.edit.resp.AttrDefVO;
import ai.plantdata.kg.api.edit.resp.AttributeDefinitionVO;
import ai.plantdata.kg.api.edit.resp.BatchRelationVO;
import ai.plantdata.kg.api.edit.resp.BatchResult;
import ai.plantdata.kg.api.pub.RelationApi;
import ai.plantdata.kg.api.pub.req.FilterRelationFrom;
import ai.plantdata.kg.api.pub.req.TripleFrom;
import ai.plantdata.kg.api.pub.resp.RelationVO;
import ai.plantdata.kg.api.pub.resp.TripleVO;
import ai.plantdata.kg.common.bean.AttributeDefinition;
import ai.plantdata.kg.common.bean.ExtraInfo;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.constant.AttributeValueType;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.constant.MetaDataInfo;
import com.plantdata.kgcloud.constant.MongoOperation;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.app.converter.RelationConverter;
import com.plantdata.kgcloud.domain.common.converter.RestCopyConverter;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.edit.req.attr.AttrConstraintsReq;
import com.plantdata.kgcloud.domain.edit.req.attr.AttrDefinitionAdditionalReq;
import com.plantdata.kgcloud.domain.edit.req.entity.TripleReq;
import com.plantdata.kgcloud.domain.edit.rsp.TripleRsp;
import com.plantdata.kgcloud.domain.edit.util.AttrConverterUtils;
import com.plantdata.kgcloud.domain.edit.util.MapperUtils;
import com.plantdata.kgcloud.sdk.req.EdgeSearchReq;
import com.plantdata.kgcloud.sdk.rsp.OpenBatchResult;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionConceptsReq;
import com.plantdata.kgcloud.domain.edit.req.attr.AttrDefinitionModifyReq;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionReq;
import com.plantdata.kgcloud.domain.edit.req.attr.AttrDefinitionSearchReq;
import com.plantdata.kgcloud.domain.edit.req.attr.AttrTemplateReq;
import com.plantdata.kgcloud.domain.edit.req.attr.EdgeAttrDefinitionReq;
import com.plantdata.kgcloud.domain.edit.req.attr.RelationAdditionalReq;
import com.plantdata.kgcloud.domain.edit.req.attr.RelationMetaReq;
import com.plantdata.kgcloud.domain.edit.req.attr.RelationSearchReq;
import com.plantdata.kgcloud.sdk.req.edit.BasicInfoReq;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionBatchRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionRsp;
import com.plantdata.kgcloud.domain.edit.rsp.AttrConstraintsRsp;
import com.plantdata.kgcloud.domain.edit.rsp.RelationRsp;
import com.plantdata.kgcloud.domain.edit.service.AttributeService;
import com.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import com.plantdata.kgcloud.domain.edit.service.ConceptService;
import com.plantdata.kgcloud.domain.edit.util.ParserBeanUtils;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionVO;
import com.plantdata.kgcloud.domain.edit.vo.IdNameVO;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.rsp.edit.EdgeSearchRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: LinHo
 * @Date: 2019/11/26 14:14
 * @Description:
 */
@Service
public class AttributeServiceImpl implements AttributeService {

    @Autowired
    private AttributeApi attributeApi;

    @Autowired
    private BatchApi batchApi;

    @Autowired
    private RelationApi relationApi;

    @Autowired
    private ConceptEntityApi conceptEntityApi;

    @Autowired
    private GraphApi graphApi;

    @Autowired
    private ConceptService conceptService;

    @Autowired
    private BasicInfoService basicInfoService;

    @Override
    public AttrDefinitionVO getAttrDetails(String kgName, Integer id) {
        Optional<AttributeDefinition> optional = RestRespConverter.convert(attributeApi.get(kgName, id));
        return optional.map(ConvertUtils.convert(AttrDefinitionVO.class))
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.ATTRIBUTE_DEFINITION_NOT_EXISTS));
    }

    @Override
    public List<AttrDefinitionRsp>  getAttrDefinitionByConceptId(String kgName,
                                                                AttrDefinitionSearchReq attrDefinitionSearchReq) {
        List<Long> ids = attrDefinitionSearchReq.getIds();
        Long conceptId = attrDefinitionSearchReq.getConceptId();
        if (0L == conceptId){
            Optional<List<AttrDefVO>> optional = RestRespConverter.convert(attributeApi.getAll(kgName));
            return optional.orElse(new ArrayList<>()).stream()
                    .map(vo -> MapperUtils.map(vo, AttrDefinitionRsp.class))
                    .collect(Collectors.toList());
        }
        ids.add(conceptId);
        AttrQueryFrom attrQueryFrom = ConvertUtils.convert(AttrQueryFrom.class).apply(attrDefinitionSearchReq);
        attrQueryFrom.setIds(ids);
        Optional<List<AttrDefVO>> optional = RestRespConverter
                .convert(attributeApi.getByConceptIds(kgName, attrQueryFrom));
        return optional.orElse(new ArrayList<>()).stream()
                .map(vo -> MapperUtils.map(vo, AttrDefinitionRsp.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AttrDefinitionRsp> getAttrDefinitionByEntityId(String kgName, Long entityId) {
        Optional<List<AttrDefVO>> optional = RestRespConverter.convert(attributeApi.getByEntityId(kgName, entityId));
        return optional.orElse(new ArrayList<>()).stream()
                .map(vo -> MapperUtils.map(vo, AttrDefinitionRsp.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AttrDefinitionRsp> getAttrDefinitionByConceptIds(String kgName,
                                                                 AttrDefinitionConceptsReq attrDefinitionConceptsReq) {
        AttrQueryFrom attrQueryFrom = ConvertUtils.convert(AttrQueryFrom.class).apply(attrDefinitionConceptsReq);
        Optional<List<AttrDefVO>> optional = RestRespConverter.convert(attributeApi.getByConceptIds(kgName,
                attrQueryFrom));
        return optional.orElse(new ArrayList<>()).stream()
                .map(vo -> MapperUtils.map(vo, AttrDefinitionRsp.class))
                .collect(Collectors.toList());
    }

    @Override
    public Integer addAttrDefinition(String kgName, AttrDefinitionReq attrDefinitionReq) {
        AttributeDefinitionFrom attributeDefinitionFrom =
                AttrConverterUtils.attrDefinitionReqConvert(attrDefinitionReq);
        attributeDefinitionFrom.setAdditionalInfo(JacksonUtils.writeValueAsString(attrDefinitionReq.getAdditionalInfo()));
        Optional<Integer> optional = RestRespConverter.convert(attributeApi.add(kgName, attributeDefinitionFrom));
        return optional.get();
    }

    @Override
    public List<AttrDefinitionBatchRsp> batchAddAttrDefinition(String kgName,
                                                               List<AttrDefinitionReq> attrDefinitionReqs) {
        List<AttributeDefinitionVO> voList =
                attrDefinitionReqs.stream().map(
                        AttrConverterUtils::attrDefinitionReqConvert
                ).collect(Collectors.toList());
        Optional<BatchResult<AttributeDefinitionVO>> optional =
                RestRespConverter.convert(batchApi.addAttributes(kgName, voList));
        return optional.map(result -> result.getError().stream()
                .map(vo -> MapperUtils.map(vo, AttrDefinitionBatchRsp.class))
                .collect(Collectors.toList())).orElse(null);
    }

    @Override
    public OpenBatchResult<AttrDefinitionBatchRsp> batchUpdate(String kgName, List<AttrDefinitionReq> attrDefinitionReqs) {
        List<AttributeDefinitionVO> voList =
                attrDefinitionReqs.stream().map(
                        AttrConverterUtils::attrDefinitionReqConvert
                ).collect(Collectors.toList());

        return RestCopyConverter.copyRestRespResult(batchApi.updateAttributes(kgName, voList), new OpenBatchResult<>());
    }

    @Override
    public void updateAttrDefinition(String kgName, AttrDefinitionModifyReq modifyReq) {
        AttributeDefinitionFrom attributeDefinitionFrom =
                AttrConverterUtils.attrDefinitionReqConvert(modifyReq);
        RestRespConverter.convertVoid(attributeApi.update(kgName, attributeDefinitionFrom));
    }

    @Override
    public void deleteAttrDefinition(String kgName, Integer id) {
        RestRespConverter.convertVoid(attributeApi.delete(kgName, id));
    }

    @Override
    public Integer addEdgeAttr(String kgName, Integer attrId, EdgeAttrDefinitionReq edgeAttrDefinitionReq) {
        EdgeFrom edgeFrom = ConvertUtils.convert(EdgeFrom.class).apply(edgeAttrDefinitionReq);
        Optional<Integer> optional = RestRespConverter.convert(attributeApi.addEdgeAttr(kgName, attrId, edgeFrom));
        return optional.get();
    }

    @Override
    public void updateEdgeAttr(String kgName, Integer attrId, Integer seqNo,
                               EdgeAttrDefinitionReq edgeAttrDefinitionReq) {

        ExtraInfo extraInfo = ConvertUtils.convert(ExtraInfo.class).apply(edgeAttrDefinitionReq);
        extraInfo.setSeqNo(seqNo);
        RestRespConverter.convertVoid(attributeApi.updateEdgeAttr(kgName, attrId, seqNo, extraInfo));
    }

    @Override
    public void deleteEdgeAttr(String kgName, Integer attrId, Integer seqNo) {
        RestRespConverter.convertVoid(attributeApi.deleteEdgeAttr(kgName, attrId, seqNo));
    }

    @Override
    public void addAttrDefinitionTemplate(String kgName, List<AttrTemplateReq> attrTemplateReqs) {
        attrTemplateReqs.stream().filter(attrTemplateReq -> AttributeValueType.isObject(attrTemplateReq.getType()))
                .forEach(attrTemplateReq -> {
                    List<IdNameVO> ranges = attrTemplateReq.getRange();
                    ranges.stream().filter(idNameVO -> Objects.isNull(idNameVO.getId()))
                            .forEach(idNameVO -> {
                                Long conceptId = basicInfoService.createBasicInfo(kgName,
                                        BasicInfoReq.builder().conceptId(0L)
                                                .type(0).name(idNameVO.getName()).build());
                                idNameVO.setId(conceptId);
                            });
                    attrTemplateReq.setDataType(0);
                    attrTemplateReq.setRange(ranges);
                });
        List<AttrDefinitionReq> attrDefinitionReqs =
                attrTemplateReqs.stream().map(attrTemplateReq -> {
                    AttrDefinitionReq attrDefinitionReq = new AttrDefinitionReq();
                    BeanUtils.copyProperties(attrTemplateReq, attrDefinitionReq);
                    attrDefinitionReq.setRangeValue(attrTemplateReq.getRange().stream().map(IdNameVO::getId).collect(Collectors.toList()));
                    return attrDefinitionReq;
                }).collect(Collectors.toList());
        this.batchAddAttrDefinition(kgName, attrDefinitionReqs);
    }

    @Override
    public Page<RelationRsp> listRelations(String kgName, RelationSearchReq relationSearchReq) {
        FilterRelationFrom filterRelationFrom = ConvertUtils.convert(FilterRelationFrom.class).apply(relationSearchReq);
        filterRelationFrom.setSkip(relationSearchReq.getPage() - 1);
        filterRelationFrom.setLimit(relationSearchReq.getSize());
        Map<String, Object> metaFilters = new HashMap<>(4);
        if (StringUtils.hasText(relationSearchReq.getSource())) {
            Map<String, Object> mongoOperation = new HashMap<>();
            mongoOperation.put(MongoOperation.EQUAL.getType(), relationSearchReq.getSource());
            metaFilters.put(MetaDataInfo.SOURCE.getCode(), mongoOperation);
        }
        if (StringUtils.hasText(relationSearchReq.getBatchNo())) {
            Map<String, Object> mongoOperation = new HashMap<>();
            mongoOperation.put(MongoOperation.EQUAL.getType(), relationSearchReq.getBatchNo());
            metaFilters.put(MetaDataInfo.BATCH_NO.getCode(), mongoOperation);
        }
        if (Objects.nonNull(relationSearchReq.getReliability())) {
            metaFilters.put(MetaDataInfo.RELIABILITY.getCode(), relationSearchReq.getReliability());
        }
        if (!metaFilters.isEmpty()) {
            filterRelationFrom.setMetaFilters(metaFilters);
        }
        //解析排序字段
        filterRelationFrom.setSort(ParserBeanUtils.parserSortMetadata(relationSearchReq.getSorts()));
        RestResp<List<RelationVO>> restResp = relationApi.listRelation(kgName, filterRelationFrom);
        Optional<List<RelationVO>> optional = RestRespConverter.convert(restResp);
        List<RelationRsp> relationRsps =
                optional.orElse(new ArrayList<>()).stream().map(ConvertUtils.convert(RelationRsp.class)).collect(Collectors.toList());
        Optional<Integer> count = RestRespConverter.convertCount(restResp);
        return new PageImpl<>(relationRsps, PageRequest.of(relationSearchReq.getPage() - 1,
                relationSearchReq.getSize()), count.get());
    }

    @Override
    public void deleteRelations(String kgName, List<String> tripleIds) {
        RestRespConverter.convertVoid(batchApi.deleteRelations(kgName, null, tripleIds));
    }

    @Override
    public void deleteRelationByMeta(String kgName, RelationMetaReq relationMetaReq) {
        Map<String, Object> filters = new HashMap<>();
        if (StringUtils.hasText(relationMetaReq.getSource())) {
            Map<String, Object> option = new HashMap<>();
            option.put(MongoOperation.EQUAL.getType(), relationMetaReq.getSource());
            filters.put(MetaDataInfo.SOURCE.getCode(), option);
        }
        if (StringUtils.hasText(relationMetaReq.getBatchNo())) {
            Map<String, Object> option = new HashMap<>();
            option.put(MongoOperation.EQUAL.getType(), relationMetaReq.getBatchNo());
            filters.put(MetaDataInfo.BATCH_NO.getCode(), option);
        }
        if (!filters.isEmpty()) {
            DeleteRelationFrom deleteRelationFrom = new DeleteRelationFrom();
            deleteRelationFrom.setFilters(filters);
            RestRespConverter.convertVoid(conceptEntityApi.deleteObjAttrValue(kgName, deleteRelationFrom));
        }
    }

    @Override
    public void upsertRelationAdditional(String kgName, RelationAdditionalReq relationAdditionalReq) {
        Map<String, Object> metaData = new HashMap<>();
        metaData.put(MetaDataInfo.ADDITIONAL.getFieldName(), relationAdditionalReq.getAdditional());
        relationAdditionalReq.setMetaData(metaData);
        UpdateRelationFrom updateRelationFrom =
                ConvertUtils.convert(UpdateRelationFrom.class).apply(relationAdditionalReq);
        RestRespConverter.convertVoid(conceptEntityApi.addObjAttrValue(kgName, updateRelationFrom));
    }

    @Override
    public void updateAttrDefinitionAdditional(String kgName, AttrDefinitionAdditionalReq additionalReq) {
        String additional = JacksonUtils.writeValueAsString(additionalReq.getAdditional());
        additionalReq.setAdditionalInfo(additional);
        AttributeDefinitionFrom attributeDefinitionFrom =
                ConvertUtils.convert(AttributeDefinitionFrom.class).apply(additionalReq);
        RestRespConverter.convertVoid(attributeApi.update(kgName, attributeDefinitionFrom));
    }

    @Override
    public List<AttrConstraintsRsp> listAttrConstraints(String kgName, AttrConstraintsReq attrConstraintsReq) {
        AttributeConstraints constraints = ConvertUtils.convert(AttributeConstraints.class).apply(attrConstraintsReq);
        Optional<List<AttrConstraintsVO>> optional = RestRespConverter.convert(graphApi.constraints(kgName,
                constraints));
        return optional.orElse(new ArrayList<>()).stream().map(ConvertUtils.convert(AttrConstraintsRsp.class)).collect(Collectors.toList());
    }

    @Override
    public void attrConstraintsDelete(String kgName, Integer attrId, List<String> tripleIds) {
        RestRespConverter.convertVoid(graphApi.constraintsDelete(kgName, attrId, tripleIds));
    }

    @Override
    public List<TripleRsp> getRelationByAttr(String kgName, TripleReq tripleReq) {
        TripleFrom tripleFrom = ConvertUtils.convert(TripleFrom.class).apply(tripleReq);
        Optional<List<TripleVO>> optional = RestRespConverter.convert(relationApi.aggRelation(kgName, tripleFrom));
        return optional.orElse(new ArrayList<>()).stream().map(vo -> MapperUtils.map(vo, TripleRsp.class)).collect(Collectors.toList());
    }

    @Override
    public List<EdgeSearchRsp> edgeSearch(String kgName, EdgeSearchReq queryReq) {
        BatchQueryRelationFrom relationFrom = RelationConverter.edgeAttrSearch(queryReq);
        Optional<List<BatchRelationVO>> resOpt = RestRespConverter.convert(batchApi.queryRelation(kgName, relationFrom));
        if (!resOpt.isPresent()) {
            return Collections.emptyList();
        }
        return BasicConverter.listConvert(resOpt.get(), RelationConverter::batchVoToEdgeSearchRsp);
    }
}
