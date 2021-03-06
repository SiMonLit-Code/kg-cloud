package ai.plantdata.kgcloud.domain.edit.service.impl;

import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.cloud.web.util.ConvertUtils;
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
import ai.plantdata.kgcloud.constant.AttributeValueType;
import ai.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import ai.plantdata.kgcloud.constant.MetaDataInfo;
import ai.plantdata.kgcloud.constant.MongoOperation;
import ai.plantdata.kgcloud.domain.app.converter.BasicConverter;
import ai.plantdata.kgcloud.domain.app.converter.RelationConverter;
import ai.plantdata.kgcloud.domain.app.service.GraphHelperService;
import ai.plantdata.kgcloud.domain.common.converter.RestCopyConverter;
import ai.plantdata.kgcloud.domain.common.util.KGUtil;
import ai.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import ai.plantdata.kgcloud.domain.edit.req.attr.AttrConstraintsReq;
import ai.plantdata.kgcloud.domain.edit.req.attr.AttrDefinitionAdditionalReq;
import ai.plantdata.kgcloud.domain.edit.req.attr.AttrTemplateReq;
import ai.plantdata.kgcloud.domain.edit.req.attr.EdgeAttrDefinitionReq;
import ai.plantdata.kgcloud.domain.edit.req.attr.RelationAdditionalReq;
import ai.plantdata.kgcloud.domain.edit.req.attr.RelationMetaReq;
import ai.plantdata.kgcloud.domain.edit.req.attr.RelationSearchMetaReq;
import ai.plantdata.kgcloud.domain.edit.req.attr.RelationSearchReq;
import ai.plantdata.kgcloud.domain.edit.req.entity.TripleReq;
import ai.plantdata.kgcloud.domain.edit.rsp.AttrConstraintsRsp;
import ai.plantdata.kgcloud.domain.edit.rsp.RelationRsp;
import ai.plantdata.kgcloud.domain.edit.rsp.TripleRsp;
import ai.plantdata.kgcloud.domain.edit.service.AttributeService;
import ai.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import ai.plantdata.kgcloud.domain.edit.service.LogSender;
import ai.plantdata.kgcloud.domain.edit.util.AttrConverterUtils;
import ai.plantdata.kgcloud.domain.edit.util.MapperUtils;
import ai.plantdata.kgcloud.domain.edit.util.ParserBeanUtils;
import ai.plantdata.kgcloud.domain.edit.vo.IdNameVO;
import ai.plantdata.kgcloud.sdk.UserClient;
import ai.plantdata.kgcloud.sdk.req.AttrDefinitionSearchReq;
import ai.plantdata.kgcloud.sdk.req.EdgeSearchReqList;
import ai.plantdata.kgcloud.sdk.req.edit.AttrDefinitionBatchRsp;
import ai.plantdata.kgcloud.sdk.req.edit.AttrDefinitionModifyReq;
import ai.plantdata.kgcloud.sdk.req.edit.AttrDefinitionReq;
import ai.plantdata.kgcloud.sdk.req.edit.AttrDefinitionVO;
import ai.plantdata.kgcloud.sdk.req.edit.BasicInfoReq;
import ai.plantdata.kgcloud.sdk.rsp.OpenBatchResult;
import ai.plantdata.kgcloud.sdk.rsp.UserDetailRsp;
import ai.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionConceptsReq;
import ai.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionRsp;
import ai.plantdata.kgcloud.sdk.rsp.edit.EdgeSearchRsp;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.plantdata.graph.logging.core.ServiceEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
    private BasicInfoService basicInfoService;
    @Autowired
    private GraphHelperService graphHelperService;

    @Autowired
    private LogSender logSender;

    @Autowired
    private UserClient userClient;

    @Override
    public AttrDefinitionVO getAttrDetails(String kgName, Integer id) {
        String dbName = KGUtil.dbName(kgName);
        RestResp<AttributeDefinition> restResp = attributeApi.get(dbName, id);

        AttributeDefinition attributeDefinition = RestRespConverter.convert(restResp)
                .orElseThrow(KgmsErrorCodeEnum.ATTRIBUTE_DEFINITION_NOT_EXISTS.error());
        return MapperUtils.map(attributeDefinition, AttrDefinitionVO.class);
    }

    @Override
    public List<AttrDefinitionRsp> getAttrDefinitionByConceptId(String kgName,
                                                                AttrDefinitionSearchReq attrDefinitionSearchReq) {
        List<Long> ids = attrDefinitionSearchReq.getIds();
        Long conceptId = attrDefinitionSearchReq.getConceptId();
        String dbName = KGUtil.dbName(kgName);
        if (conceptId == null || 0L == conceptId) {
            RestResp<List<AttrDefVO>> restResp = attributeApi.getAll(dbName);
            return RestRespConverter
                    .convert(restResp)
                    .orElse(new ArrayList<>()).stream()
                    .map(vo -> MapperUtils.map(vo, AttrDefinitionRsp.class))
                    .collect(Collectors.toList());
        }
        ids.add(conceptId);
        AttrQueryFrom attrQueryFrom = ConvertUtils.convert(AttrQueryFrom.class).apply(attrDefinitionSearchReq);
        attrQueryFrom.setIds(ids);
        RestResp<List<AttrDefVO>> restResp = attributeApi.getByConceptIds(dbName, attrQueryFrom);
        return RestRespConverter
                .convert(restResp)
                .orElse(new ArrayList<>()).stream()
                .map(vo -> MapperUtils.map(vo, AttrDefinitionRsp.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AttrDefinitionRsp> getAttrDefinitionByEntityId(String kgName, Long entityId) {
        String dbName = KGUtil.dbName(kgName);
        RestResp<List<AttrDefVO>> restResp = attributeApi.getByEntityId(dbName, entityId);
        return RestRespConverter
                .convert(restResp)
                .orElse(new ArrayList<>()).stream()
                .map(vo -> MapperUtils.map(vo, AttrDefinitionRsp.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AttrDefinitionRsp> getAttrDefinitionByConceptIds(String kgName,
                                                                 AttrDefinitionConceptsReq attrDefinitionConceptsReq) {
        AttrQueryFrom attrQueryFrom = ConvertUtils.convert(AttrQueryFrom.class).apply(attrDefinitionConceptsReq);
        String dbName = KGUtil.dbName(kgName);
        RestResp<List<AttrDefVO>> restResp = attributeApi.getByConceptIds(dbName, attrQueryFrom);
        return RestRespConverter.convert(restResp).orElse(new ArrayList<>()).stream()
                .map(vo -> MapperUtils.map(vo, AttrDefinitionRsp.class))
                .collect(Collectors.toList());
    }

    @Override
    public Integer addAttrDefinition(String kgName, AttrDefinitionReq attrDefinitionReq) {
        AttributeDefinitionFrom attributeDefinitionFrom =
                AttrConverterUtils.attrDefinitionReqConvert(attrDefinitionReq);
        attributeDefinitionFrom.setAdditionalInfo(JacksonUtils.writeValueAsString(attrDefinitionReq.getAdditionalInfo()));
        String dbName = KGUtil.dbName(kgName);
        RestResp<Integer> restResp = attributeApi.add(dbName,
                attributeDefinitionFrom);
        return RestRespConverter.convert(restResp).orElse(null);
    }

    @Override
    public OpenBatchResult<AttrDefinitionBatchRsp> batchAddAttrDefinition(String kgName,
                                                                          List<AttrDefinitionReq> attrDefinitionReqs) {
        List<AttributeDefinitionVO> voList = attrDefinitionReqs.stream()
                .map(AttrConverterUtils::attrDefinitionReqConvert)
                .collect(Collectors.toList());
        String dbName = KGUtil.dbName(kgName);
        RestResp<BatchResult<AttributeDefinitionVO>> restResp = batchApi.addAttributes(dbName, voList);
        return RestRespConverter.convert(restResp)
                .map((a) -> {
                    List<AttrDefinitionBatchRsp> success = MapperUtils.map(a.getSuccess(), AttrDefinitionBatchRsp.class);
                    List<AttrDefinitionBatchRsp> error = MapperUtils.map(a.getError(), AttrDefinitionBatchRsp.class);
                    return new OpenBatchResult<>(success, error);
                })
                .orElse(OpenBatchResult.empty());
    }

    @Override
    public OpenBatchResult<AttrDefinitionBatchRsp> batchUpdate(String kgName,
                                                               List<AttrDefinitionModifyReq> attrDefinitionReqs) {
        List<AttributeDefinitionVO> voList = attrDefinitionReqs.stream()
                .map(AttrConverterUtils::attrDefinitionReqConvert)
                .collect(Collectors.toList());
        RestResp<BatchResult<AttributeDefinitionVO>> restResp = batchApi.updateAttributes(KGUtil.dbName(kgName), voList);
        return RestRespConverter.convert(restResp)
                .map(result -> RestCopyConverter.copyToBatchResult(result, AttrDefinitionBatchRsp.class))
                .orElseGet(OpenBatchResult::new);
    }

    @Override
    public void updateAttrDefinition(String kgName, AttrDefinitionModifyReq modifyReq) {
        AttributeDefinitionFrom attributeDefinitionFrom =
                AttrConverterUtils.attrDefinitionReqConvert(modifyReq);
        String dbName = KGUtil.dbName(kgName);
        RestResp restResp = attributeApi.update(dbName, attributeDefinitionFrom);
        RestRespConverter.convertVoid(restResp);
    }

    @Override
    public void deleteAttrDefinition(String kgName, Integer id) {
        String dbName = KGUtil.dbName(kgName);
        RestResp restResp = attributeApi.delete(dbName, id);
        RestRespConverter.convertVoid(restResp);
    }

    @Override
    public Integer addEdgeAttr(String kgName, Integer attrId, EdgeAttrDefinitionReq edgeAttrDefinitionReq) {
        EdgeFrom edgeFrom = ConvertUtils.convert(EdgeFrom.class).apply(edgeAttrDefinitionReq);
        String dbName = KGUtil.dbName(kgName);
        RestResp<Integer> restResp = attributeApi.addEdgeAttr(dbName, attrId, edgeFrom);
        return RestRespConverter.convert(restResp).orElse(null);
    }

    @Override
    public void updateEdgeAttr(String kgName, Integer attrId, Integer seqNo,
                               EdgeAttrDefinitionReq edgeAttrDefinitionReq) {

        ExtraInfo extraInfo = ConvertUtils.convert(ExtraInfo.class).apply(edgeAttrDefinitionReq);
        extraInfo.setSeqNo(seqNo);
        RestRespConverter.convertVoid(attributeApi.updateEdgeAttr(KGUtil.dbName(kgName), attrId, seqNo, extraInfo));
    }

    @Override
    public void deleteEdgeAttr(String kgName, Integer attrId, Integer seqNo) {
        RestRespConverter.convertVoid(attributeApi.deleteEdgeAttr(KGUtil.dbName(kgName), attrId, seqNo));
    }

    @Override
    public void addAttrDefinitionTemplate(String kgName, List<AttrTemplateReq> attrTemplateReqs) {
        Set<String> rangeNames = Sets.newHashSet();
        attrTemplateReqs.stream().filter(attrTemplateReq -> AttributeValueType.isObject(attrTemplateReq.getType()))
                .forEach(attrTemplateReq -> {
                    List<IdNameVO> ranges = attrTemplateReq.getRange();
                    if (!CollectionUtils.isEmpty(ranges)) {
                        ranges.stream().filter(idNameVO -> Objects.isNull(idNameVO.getId()))
                                .forEach(idNameVO -> rangeNames.add(idNameVO.getName()));
                    }
                });
        Map<String, Long> idNameMap = Maps.newHashMap();
        rangeNames.forEach(name -> {
            Long conceptId = basicInfoService.createBasicInfo(KGUtil.dbName(kgName),
                    BasicInfoReq.builder().conceptId(0L).type(0).name(name).build(), ServiceEnum.SCHEMA_REPO);
            idNameMap.put(name, conceptId);
        });
        attrTemplateReqs.stream().filter(attrTemplateReq -> AttributeValueType.isObject(attrTemplateReq.getType()))
                .forEach(attrTemplateReq -> {
                    List<IdNameVO> ranges = attrTemplateReq.getRange();
                    ranges.stream().filter(idNameVO -> Objects.isNull(idNameVO.getId()))
                            .forEach(idNameVO -> idNameVO.setId(idNameMap.get(idNameVO.getName())));
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
        logSender.setActionId();
        logSender.sendLog(kgName, ServiceEnum.SCHEMA_REPO);
        this.batchAddAttrDefinition(kgName, attrDefinitionReqs);
        logSender.remove();
    }

    @Override
    public Page<RelationRsp> listRelations(String kgName, RelationSearchReq relationSearchReq,
                                           RelationSearchMetaReq metaReq) {
        FilterRelationFrom filterRelationFrom = ConvertUtils.convert(FilterRelationFrom.class).apply(relationSearchReq);
        Integer size = relationSearchReq.getSize();
        Integer page = (relationSearchReq.getPage() - 1) * size;
        filterRelationFrom.setSkip(page);
        filterRelationFrom.setLimit(size + 1);
        Map<String, Object> metaFilters = new HashMap<>(4);
        if (StringUtils.hasText(metaReq.getSource())) {
            Map<String, Object> mongoOperation = new HashMap<>();
            mongoOperation.put(MongoOperation.EQUAL.getType(), metaReq.getSource());
            metaFilters.put(MetaDataInfo.SOURCE.getCode(), mongoOperation);
        }
        if (StringUtils.hasText(metaReq.getBatchNo())) {
            Map<String, Object> mongoOperation = new HashMap<>();
            mongoOperation.put(MongoOperation.EQUAL.getType(), metaReq.getBatchNo());
            metaFilters.put(MetaDataInfo.BATCH_NO.getCode(), mongoOperation);
        }
        if (Objects.nonNull(metaReq.getReliability()) && !metaReq.getReliability().isEmpty()) {
            metaFilters.put(MetaDataInfo.RELIABILITY.getCode(), metaReq.getReliability());
        }
        if (Objects.nonNull(metaReq.getSourceUser()) && !metaReq.getSourceUser().isEmpty()) {
            metaFilters.put(MetaDataInfo.SOURCE_USER.getCode(), metaReq.getSourceUser());
        }
        if (Objects.nonNull(metaReq.getSourceAction()) && !metaReq.getSourceAction().isEmpty()) {
            metaFilters.put(MetaDataInfo.SOURCE_ACTION.getCode(), metaReq.getSourceAction());
        }
        if (!metaFilters.isEmpty()) {
            filterRelationFrom.setMetaFilters(metaFilters);
        }
        //解析排序字段
        filterRelationFrom.setSort(ParserBeanUtils.parserSortMetadata(relationSearchReq.getSorts()));
        RestResp<List<RelationVO>> restResp = relationApi.listRelation(KGUtil.dbName(kgName), filterRelationFrom);
        Optional<List<RelationVO>> optional = RestRespConverter.convert(restResp);
        List<RelationRsp> relationRsps =
                optional.orElse(new ArrayList<>()).stream().map(ParserBeanUtils::parserRelationMeta).collect(Collectors.toList());
        int count = relationRsps.size();
        if (count > size) {
            relationRsps.remove(size.intValue());
            count += page;
        }

        Map<String, String> usernameMap = new HashMap<>();
        relationRsps.forEach(relationRsp -> {
            if (relationRsp.getSourceUser() != null && !relationRsp.getSourceUser().isEmpty()) {
                if (!usernameMap.containsKey(relationRsp.getSourceUser())) {
                    UserDetailRsp userDetailRsp = userClient.getCurrentUserIdDetail(relationRsp.getSourceUser()).getData();
                    usernameMap.put(userDetailRsp.getId(), userDetailRsp.getRealname());
                }
                relationRsp.setSourceUser(usernameMap.get(relationRsp.getSourceUser()));
            }
        });
        return new PageImpl<>(relationRsps, PageRequest.of(relationSearchReq.getPage() - 1,
                size), count);
    }

    @Override
    public void deleteRelations(String kgName, Boolean isTrace, List<String> tripleIds) {
        logSender.setActionId();
        if (isTrace) {
            logSender.sendLog(kgName, ServiceEnum.RELATION_TRACE);
        } else {
            logSender.sendLog(kgName, ServiceEnum.RELATION_EDIT);
        }
        RestRespConverter.convertVoid(batchApi.deleteRelations(KGUtil.dbName(kgName), null, tripleIds));
        logSender.remove();
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
            RestRespConverter.convertVoid(conceptEntityApi.deleteObjAttrValue(KGUtil.dbName(kgName),
                    deleteRelationFrom));
        }
    }

    @Override
    public void upsertRelationAdditional(String kgName, RelationAdditionalReq relationAdditionalReq) {
        Map<String, Object> metaData = new HashMap<>();
        metaData.put(MetaDataInfo.ADDITIONAL.getFieldName(), relationAdditionalReq.getAdditional());
        relationAdditionalReq.setMetaData(metaData);
        UpdateRelationFrom updateRelationFrom =
                ConvertUtils.convert(UpdateRelationFrom.class).apply(relationAdditionalReq);
        RestRespConverter.convertVoid(conceptEntityApi.addObjAttrValue(KGUtil.dbName(kgName), updateRelationFrom));
    }

    @Override
    public void updateAttrDefinitionAdditional(String kgName, AttrDefinitionAdditionalReq additionalReq) {
        String additional = JacksonUtils.writeValueAsString(additionalReq.getAdditional());
        additionalReq.setAdditionalInfo(additional);
        AttributeDefinitionFrom attributeDefinitionFrom =
                ConvertUtils.convert(AttributeDefinitionFrom.class).apply(additionalReq);
        RestRespConverter.convertVoid(attributeApi.update(KGUtil.dbName(kgName), attributeDefinitionFrom));
    }

    @Override
    public List<AttrConstraintsRsp> listAttrConstraints(String kgName, AttrConstraintsReq attrConstraintsReq) {
        AttributeConstraints constraints = ConvertUtils.convert(AttributeConstraints.class).apply(attrConstraintsReq);
        Optional<List<AttrConstraintsVO>> optional =
                RestRespConverter.convert(graphApi.constraints(KGUtil.dbName(kgName),
                        constraints));
        return optional.orElse(new ArrayList<>()).stream().map(ConvertUtils.convert(AttrConstraintsRsp.class)).collect(Collectors.toList());
    }

    @Override
    public void attrConstraintsDelete(String kgName, Integer attrId, List<String> tripleIds) {
        RestRespConverter.convertVoid(graphApi.constraintsDelete(KGUtil.dbName(kgName), attrId, tripleIds));
    }

    @Override
    public List<TripleRsp> getRelationByAttr(String kgName, TripleReq tripleReq) {
        TripleFrom tripleFrom = ConvertUtils.convert(TripleFrom.class).apply(tripleReq);
        Optional<List<TripleVO>> optional = RestRespConverter.convert(relationApi.aggRelation(KGUtil.dbName(kgName),
                tripleFrom));
        return optional.orElse(new ArrayList<>()).stream().map(vo -> MapperUtils.map(vo, TripleRsp.class)).collect(Collectors.toList());
    }

    @Override
    public List<EdgeSearchRsp> edgeSearch(String kgName, EdgeSearchReqList queryReq) {
        graphHelperService.replaceByAttrKey(kgName, queryReq);
        BatchQueryRelationFrom relationFrom = RelationConverter.edgeAttrSearch(queryReq);
        Optional<List<BatchRelationVO>> resOpt = RestRespConverter.convert(batchApi.queryRelation(KGUtil.dbName(kgName),
                relationFrom));
        if (!resOpt.isPresent()) {
            return Collections.emptyList();
        }
        return BasicConverter.listConvert(resOpt.get(), RelationConverter::batchVoToEdgeSearchRsp);
    }
}
