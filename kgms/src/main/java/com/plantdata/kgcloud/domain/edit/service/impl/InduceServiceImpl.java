package com.plantdata.kgcloud.domain.edit.service.impl;

import ai.plantdata.kg.api.edit.InduceApi;
import ai.plantdata.kg.api.edit.req.ExecuteInduceConceptFrom;
import ai.plantdata.kg.api.edit.req.InduceAttributeFrom;
import ai.plantdata.kg.api.edit.resp.AutoRecommendVO;
import ai.plantdata.kg.api.edit.resp.InduceConceptVO;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.constant.BasicInfoType;
import com.plantdata.kgcloud.constant.InduceType;
import com.plantdata.kgcloud.domain.common.util.KGUtil;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.edit.req.attr.AttrDefinitionSearchReq;
import com.plantdata.kgcloud.domain.edit.req.induce.AttrInduceReq;
import com.plantdata.kgcloud.domain.edit.req.induce.AttrSearchReq;
import com.plantdata.kgcloud.domain.edit.req.induce.InduceConceptReq;
import com.plantdata.kgcloud.domain.edit.req.induce.InduceMergeReq;
import com.plantdata.kgcloud.domain.edit.req.induce.InduceObjectReq;
import com.plantdata.kgcloud.domain.edit.req.induce.InducePublicReq;
import com.plantdata.kgcloud.domain.edit.rsp.AttrInduceFindRsp;
import com.plantdata.kgcloud.domain.edit.rsp.BasicInfoRsp;
import com.plantdata.kgcloud.domain.edit.rsp.InduceConceptRsp;
import com.plantdata.kgcloud.domain.edit.service.AttributeService;
import com.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import com.plantdata.kgcloud.domain.edit.service.EntityService;
import com.plantdata.kgcloud.domain.edit.service.InduceService;
import com.plantdata.kgcloud.domain.edit.util.InduceParserUtils;
import com.plantdata.kgcloud.domain.edit.util.MapperUtils;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionVO;
import com.plantdata.kgcloud.sdk.req.edit.BasicInfoReq;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: LinHo
 * @Date: 2019/11/29 14:10
 * @Description:
 */
@Service
public class InduceServiceImpl implements InduceService {

    @Autowired
    private InduceApi induceApi;

    @Autowired
    private BasicInfoService basicInfoService;

    @Autowired
    private AttributeService attributeService;

    @Autowired
    private EntityService entityService;

    @Override
    public Page<AttrDefinitionVO> induceAttributeList(String kgName, AttrSearchReq attrSearchReq) {
        RestResp<List<AutoRecommendVO>> restResp = induceApi.privateAttributeList(KGUtil.dbName(kgName),
                attrSearchReq.getConceptId(), attrSearchReq.getAttrName(),
                attrSearchReq.getType(), attrSearchReq.getPage() - 1, attrSearchReq.getSize());
        Optional<List<AutoRecommendVO>> optional = RestRespConverter.convert(restResp);
        Optional<Integer> count = RestRespConverter.convertCount(restResp);
        List<AttrInduceFindRsp> induceFindRsps =
                optional.orElse(new ArrayList<>()).stream().map(autoRecommendVO -> MapperUtils.map(autoRecommendVO,
                        AttrInduceFindRsp.class)).collect(Collectors.toList());
        List<AttrDefinitionVO> attrDefinitionList = induceFindRsps.stream()
                .filter(attrInduceFindRsp -> Objects.nonNull(attrInduceFindRsp.getAttrList()) && !attrInduceFindRsp
                        .getAttrList().isEmpty()).map(attrInduceFindRsp -> attrInduceFindRsp.getAttrList().get(0))
                .collect(Collectors.toList());
        return new PageImpl<>(attrDefinitionList, PageRequest.of(attrSearchReq.getPage() - 1,
                attrSearchReq.getSize()), count.orElse(0));
    }

    @Override
    public List<AttrInduceFindRsp> induceAttributeFind(String kgName, AttrInduceReq attrInduceReq) {
        RestResp<List<AutoRecommendVO>> restResp = induceApi.findAttributeToInduce(KGUtil.dbName(kgName),
                attrInduceReq.getConceptId(), attrInduceReq.getType(), 0D);
        Optional<List<AutoRecommendVO>> optional = RestRespConverter.convert(restResp);
        List<AttrInduceFindRsp> induceFindRsps =
                optional.orElse(new ArrayList<>()).stream().map(autoRecommendVO -> MapperUtils.map(autoRecommendVO,
                        AttrInduceFindRsp.class)).collect(Collectors.toList());
        Integer number = attrInduceReq.getNumber();
        if (Objects.nonNull(attrInduceReq.getType()) && !InduceType.isObject(attrInduceReq.getType()) &&
                !InduceType.isPublic(attrInduceReq.getType())) {
            number = null;
        }
        return InduceParserUtils.attrFindItemAddMsg(induceFindRsps, number);
    }

    @Override
    public void inducePublic(String kgName, InducePublicReq inducePublicReq) {
        InduceAttributeFrom induceAttributeFrom =
                ConvertUtils.convert(InduceAttributeFrom.class).apply(inducePublicReq);
        RestRespConverter.convertVoid(induceApi.induceAttribute(KGUtil.dbName(kgName), induceAttributeFrom));
    }

    @Override
    public void induceObject(String kgName, InduceObjectReq induceObjectReq) {
        RestRespConverter.convertVoid(induceApi.instantiateAttribute(KGUtil.dbName(kgName),
                induceObjectReq.getAttributeId(),
                induceObjectReq.getAttributeName(), induceObjectReq.getRangeId()));
    }

    @Override
    public void induceMerge(String kgName, InduceMergeReq induceMergeReq) {
        RestRespConverter.convertVoid(induceApi.mergeAttribute(KGUtil.dbName(kgName), induceMergeReq.getAttributeId(),
                induceMergeReq.getSrcAttrIds(), induceMergeReq.getType()));
    }

    @Override
    public List<InduceConceptRsp> listInduceConcept(String kgName, Long conceptId) {
        Optional<List<InduceConceptVO>> optional =
                RestRespConverter.convert(induceApi.conceptToInduce(KGUtil.dbName(kgName),
                        conceptId));
        //TODO 是否兼容url
        List<AttrDefinitionRsp> attrDefinitionRsps = attributeService.getAttrDefinitionByConceptId(kgName,
                new AttrDefinitionSearchReq(true, conceptId, 0));
        Map<Integer, AttrDefinitionRsp> attrDefinitionRspMap = attrDefinitionRsps.stream()
                .collect(Collectors.toMap(AttrDefinitionRsp::getId, Function.identity(), (k1, k2) -> k1));
        return optional.orElse(new ArrayList<>()).stream()
                .map(vo -> {
                    InduceConceptRsp induceConceptRsp = new InduceConceptRsp();
                    induceConceptRsp.setParentId(vo.getParentId());
                    induceConceptRsp.setConceptName(vo.getConceptName());
                    List<Map<Integer, List<String>>> dataAttributeValueList = vo.getDataAttributeValueList();
                    if (!CollectionUtils.isEmpty(dataAttributeValueList)) {
                        induceConceptRsp.setDataAttributeValueList(dataAttributeValueList.stream().map(f -> {
                            Map<String, Object> data = new HashMap<>();
                            Map.Entry<Integer, List<String>> kv = f.entrySet().iterator().next();
                            data.put("id", kv.getKey());
                            data.put("value", kv.getValue());
                            data.put("name", attrDefinitionRspMap.get(kv.getKey()).getName());
                            return data;
                        }).collect(Collectors.toList()));
                    }
                    List<Map<Integer, List<Long>>> objectAttributeValueList = vo.getObjectAttributeValueList();
                    if (objectAttributeValueList != null) {
                        List<Long> ids = new ArrayList<>();
                        objectAttributeValueList.forEach(x -> {
                            x.forEach((k, v) -> ids.addAll(v));
                        });
                        if (!ids.isEmpty()) {
                            Map<Long, String> nameMap = basicInfoService.listByIds(kgName, ids)
                                    .stream().collect(Collectors.toMap(BasicInfoRsp::getId, BasicInfoRsp::getName,
                                            (k1, k2) -> k1));
                            induceConceptRsp.setObjectAttributeValueList(objectAttributeValueList.stream().map(f -> {
                                Map<String, Object> data = new HashMap<>();
                                Map.Entry<Integer, List<Long>> kv = f.entrySet().iterator().next();
                                data.put("id", kv.getKey());
                                data.put("value", kv.getValue().stream().map(x -> {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("id", x);
                                    map.put("name", nameMap.get(x));
                                    return map;
                                }).collect(Collectors.toList()));
                                data.put("name", attrDefinitionRspMap.get(kv.getKey()).getName());
                                data.put("range", attrDefinitionRspMap.get(kv.getKey()).getRangeValue());
                                return data;
                            }).collect(Collectors.toList()));
                        }
                    }
                    return induceConceptRsp;
                }).collect(Collectors.toList());

    }

    @Override
    public void induceConcept(String kgName, InduceConceptReq induceConceptReq) {
        Long conceptId = induceConceptReq.getConceptId();
        if (Objects.isNull(conceptId)) {
            conceptId = basicInfoService.createBasicInfo(KGUtil.dbName(kgName), BasicInfoReq.builder()
                    .name(induceConceptReq.getConceptName()).conceptId(induceConceptReq.getParentId())
                    .type(BasicInfoType.CONCEPT.getType()).build());
        }
        ExecuteInduceConceptFrom executeInduceConceptFrom =
                ConvertUtils.convert(ExecuteInduceConceptFrom.class).apply(induceConceptReq);
        RestRespConverter.convertVoid(induceApi.induceConcept(KGUtil.dbName(kgName), executeInduceConceptFrom));
    }
}
