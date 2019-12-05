package com.plantdata.kgcloud.domain.edit.service.impl;

import ai.plantdata.kg.api.edit.MergeApi;
import ai.plantdata.kg.api.edit.req.CompareModel;
import ai.plantdata.kg.api.edit.req.EntityMergeFrom;
import ai.plantdata.kg.api.edit.req.MergeFrom;
import ai.plantdata.kg.common.bean.EntityMerge;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.constant.AttributeValueType;
import com.plantdata.kgcloud.constant.MergeType;
import com.plantdata.kgcloud.constant.MetaDataInfo;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.edit.req.merge.EntityMergeReq;
import com.plantdata.kgcloud.domain.edit.req.merge.MergeRecommendedReq;
import com.plantdata.kgcloud.domain.edit.req.merge.RecommendedMergeReq;
import com.plantdata.kgcloud.domain.edit.rsp.BasicInfoRsp;
import com.plantdata.kgcloud.domain.edit.rsp.EntityMergeRsp;
import com.plantdata.kgcloud.domain.edit.service.AttributeService;
import com.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import com.plantdata.kgcloud.domain.edit.service.MergeService;
import com.plantdata.kgcloud.domain.edit.vo.EntityAttrValueVO;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: LinHo
 * @Date: 2019/11/28 15:03
 * @Description:
 */
@Service
public class MergeServiceImpl implements MergeService {

    @Autowired
    private MergeApi mergeApi;

    @Autowired
    private BasicInfoService basicInfoService;

    @Autowired
    private AttributeService attributeService;

    @Override
    public void entityMerge(String kgName, EntityMergeReq entityMergeReq) {
        MergeFrom mergeFrom = ConvertUtils.convert(MergeFrom.class).apply(entityMergeReq);
        if (!MergeType.SUBJECT.getType().equals(entityMergeReq.getType())) {
            Map<Integer, Object> mergeRule = getMergeRule(kgName, entityMergeReq);
            mergeFrom.setDataAttrValues(mergeRule);
        }
        RestRespConverter.convertVoid(mergeApi.entity(kgName, mergeFrom));
    }

    @Override
    public Page<EntityMergeRsp> listMerges(String kgName, MergeRecommendedReq mergeRecommendedReq) {
        EntityMergeFrom entityMergeFrom = ConvertUtils.convert(EntityMergeFrom.class).apply(mergeRecommendedReq);
        CompareModel compareModel = new CompareModel();
        compareModel.setField(MetaDataInfo.SCORE.getName());
        compareModel.setMin(mergeRecommendedReq.getMinScore());
        compareModel.setMax(mergeRecommendedReq.getMaxScore());
        entityMergeFrom.setCompareModel(compareModel);
        entityMergeFrom.setSkip(mergeRecommendedReq.getPage() - 1);
        entityMergeFrom.setLimit(mergeRecommendedReq.getSize());
        RestResp<List<EntityMerge>> restResp = mergeApi.list(kgName, entityMergeFrom);
        Optional<List<EntityMerge>> optional = RestRespConverter.convert(restResp);
        Optional<Integer> count = RestRespConverter.convertCount(restResp);
        List<EntityMergeRsp> mergeRsps = optional.orElse(new ArrayList<>()).stream()
                .map(ConvertUtils.convert(EntityMergeRsp.class)).collect(Collectors.toList());
        return new PageImpl<>(mergeRsps, PageRequest.of(mergeRecommendedReq.getPage() - 1,
                mergeRecommendedReq.getSize()), count.get());
    }

    /**
     * 数值属性值融合的规则
     *
     * @param kgName
     * @param entityMergeReq
     * @return
     */
    private Map<Integer, Object> getMergeRule(String kgName, EntityMergeReq entityMergeReq) {
        Map<Integer, Object> dataAttrValues = new HashMap<>();
        BasicInfoRsp basicInfoRsp = basicInfoService.getDetails(kgName, entityMergeReq.getEntityId());
        List<EntityAttrValueVO> attrValue = basicInfoRsp.getAttrValue();
        List<EntityAttrValueVO> collect = attrValue.stream()
                .filter(entityAttributeValueVO -> Objects.isNull(entityAttributeValueVO.getDataValue()))
                .collect(Collectors.toList());
        if (collect.isEmpty()) {
            return dataAttrValues;
        } else {
            List<BasicInfoRsp> basicInfoRsps = basicInfoService.listByIds(kgName, entityMergeReq.getToMergeEntityIds());
            if (MergeType.SCORE.getType().equals(entityMergeReq.getType())) {
                List<BasicInfoRsp> infoRsps = basicInfoRsps.stream()
                        .filter(rsp -> Objects.nonNull(rsp.getScore()))
                        .sorted(Comparator.comparing(BasicInfoRsp::getScore).reversed())
                        .collect(Collectors.toList());
                if (!infoRsps.isEmpty()) {
                    BasicInfoRsp infoRsp = infoRsps.get(0);
                    infoRsp.getAttrValue().stream()
                            .filter(vo -> AttributeValueType.isNumeric(vo.getType()))
                            .forEach(vo -> dataAttrValues.put(vo.getId(), vo.getDataValue()));
                }
                return dataAttrValues;
            } else if (MergeType.SOURCE.getType().equals(entityMergeReq.getType())) {
                if (StringUtils.hasText(entityMergeReq.getSource())) {
                    for (BasicInfoRsp rsp : basicInfoRsps) {
                        if (entityMergeReq.getSource().equals(rsp.getSource())) {
                            rsp.getAttrValue().stream()
                                    .filter(vo -> AttributeValueType.isNumeric(vo.getType()))
                                    .forEach(vo -> dataAttrValues.put(vo.getId(), vo.getDataValue()));
                            return dataAttrValues;
                        }
                    }
                }
            }
        }
        return dataAttrValues;
    }

    @Override
    public void recommendedMerge(String kgName, List<RecommendedMergeReq> recommendedMergeReqs) {
        List<MergeFrom> mergeFroms = recommendedMergeReqs.stream()
                .map(recommendedMergeReq -> {
                    MergeFrom mergeFrom = new MergeFrom();
                    BeanUtils.copyProperties(recommendedMergeReq, mergeFrom);
                    mergeFrom.setMode(0);
                    return mergeFrom;
                }).collect(Collectors.toList());
        mergeFroms.forEach(mergeFrom -> RestRespConverter.convertVoid(mergeApi.entity(kgName, mergeFrom)));
    }
}
