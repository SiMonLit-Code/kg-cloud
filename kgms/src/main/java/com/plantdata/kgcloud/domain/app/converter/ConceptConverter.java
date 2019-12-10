package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.edit.resp.AttrDefVO;
import ai.plantdata.kg.common.bean.BasicInfo;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.constant.MetaDataInfo;
import com.plantdata.kgcloud.domain.app.util.DefaultUtils;
import com.plantdata.kgcloud.sdk.rsp.app.main.BasicConceptRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.BasicConceptTreeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.AdditionalRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.BaseConceptRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.NonNull;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 15:16
 */
public class ConceptConverter {


    public static String getKgTittle(@NonNull List<BasicInfo> conceptList) {
        return conceptList.stream().filter(a -> a.getId() == 0).findFirst().orElse(new BasicInfo()).getName();
    }

    public static List<BaseConceptRsp> voToRsp(@NonNull List<BasicInfo> conceptList) {
        List<BaseConceptRsp> baseConceptRspList = Lists.newArrayListWithCapacity(conceptList.size());
        BaseConceptRsp conceptRsp;
        for (BasicInfo basicInfo : conceptList) {
            if (basicInfo.getId() == 0) {
                continue;
            }
            conceptRsp = new BaseConceptRsp();
            conceptRsp.setId(basicInfo.getId());
            conceptRsp.setKey(basicInfo.getKey());
            conceptRsp.setName(basicInfo.getName());
            conceptRsp.setImg(basicInfo.getImageUrl());
            conceptRsp.setParentId(basicInfo.getConceptId());
            Map<String, Object> metaData = basicInfo.getMetaData();
            if (!CollectionUtils.isEmpty(metaData)) {
                if (metaData.containsKey(MetaDataInfo.OPEN_GIS.getFieldName())) {
                    conceptRsp.setOpenGis((Boolean) metaData.get(MetaDataInfo.OPEN_GIS.getFieldName()));
                }
                if (metaData.containsKey(MetaDataInfo.ADDITIONAL.getFieldName())) {
                    conceptRsp.setAdditional(JacksonUtils.readValue(metaData.get(MetaDataInfo.ADDITIONAL.getFieldName()).toString(), AdditionalRsp.class));
                }
            }
            baseConceptRspList.add(conceptRsp);
        }
        return baseConceptRspList;
    }

    public static List<BasicConceptRsp> voToBasic(@NonNull List<BasicInfo> conceptList) {
        return conceptList.stream().map(ConceptConverter::basicInfoToConcept).collect(Collectors.toList());
    }

    public static BasicConceptTreeRsp voToConceptTree(@NonNull List<BasicInfo> conceptList, BasicConceptTreeRsp treeRsp) {
        return voToConceptTree(conceptList, Collections.emptyList(), treeRsp);
    }

    public static BasicConceptTreeRsp voToConceptTree(@NonNull List<BasicInfo> conceptList, List<AttrDefVO> attrDefList, BasicConceptTreeRsp treeRsp) {


        List<BasicConceptTreeRsp> allConceptList = conceptList.stream().map(ConceptConverter::basicInfoToConcept).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(allConceptList)) {
            return treeRsp;
        }

        if (!CollectionUtils.isEmpty(attrDefList)) {
            fillAttrDef(allConceptList, attrDefList);
        }
        Map<Long, List<BasicConceptTreeRsp>> parentTreeItemMap = allConceptList.stream().collect(Collectors.groupingBy(BasicConceptTreeRsp::getParentId));

        fillTree(Lists.newArrayList(treeRsp), parentTreeItemMap);
        return treeRsp;
    }

    /**
     * 获取非零顶层概念
     *
     * @param conceptId
     * @param conceptMap
     * @return
     */
    public static BasicInfo getTopConcept(Long conceptId, Map<Long, BasicInfo> conceptMap) {
        BasicInfo basicInfo = conceptMap.get(conceptId);
        if ((basicInfo == null) || basicInfo.getConceptId() == null || (basicInfo.getConceptId() == 0)) {
            return basicInfo;
        }
        return getTopConcept(basicInfo.getConceptId(), conceptMap);
    }

    public static List<Long> getAllParentConceptId(List<Long> allConceptIdList, Long conceptId, Map<Long, BasicInfo> conceptMap) {
        BasicInfo basicInfo = conceptMap.get(conceptId);
        if (basicInfo != null && basicInfo.getConceptId() != null && basicInfo.getConceptId() > 0) {
            allConceptIdList.add(basicInfo.getConceptId());
            getAllParentConceptId(allConceptIdList, basicInfo.getConceptId(), conceptMap);
        }
        return allConceptIdList;
    }

    private static void fillAttrDef(@NonNull List<BasicConceptTreeRsp> allConceptList, @NonNull List<AttrDefVO> attrDefList) {
        Map<Long, BasicConceptTreeRsp> conceptTreeRspMap = allConceptList.stream().collect(Collectors.toMap(BasicConceptTreeRsp::getId, Function.identity()));
        Map<Long, List<AttrDefVO>> groupBuConceptMap = attrDefList.stream().collect(Collectors.groupingBy(AttrDefVO::getDomainValue));
        allConceptList.forEach(a -> {
            List<AttrDefVO> attrDefByConceptList = groupBuConceptMap.get(a.getId());
            if (!CollectionUtils.isEmpty(attrDefByConceptList)) {
                attrDefByConceptList.forEach(attrDef -> {
                    BasicConceptTreeRsp.NumberAttr numberAttr = new BasicConceptTreeRsp.NumberAttr(attrDef.getId(), attrDef.getName(), attrDef.getDomainValue(), attrDef.getDataType());
                    if (CollectionUtils.isEmpty(attrDef.getRangeValue())) {
                        return;
                    }
                    attrDef.getRangeValue().forEach(range -> {
                        BasicConceptTreeRsp conceptTreeRsp = conceptTreeRspMap.get(range);
                        if (null != conceptTreeRsp) {
                            DefaultUtils.listAdd(conceptTreeRsp.getNumAttrs(), numberAttr);
                            DefaultUtils.listAdd(a.getChildren(), conceptTreeRsp);
                        }
                    });

                });
            }
        });
    }

    private static BasicConceptTreeRsp basicInfoToConcept(BasicInfo concept) {
        BasicConceptTreeRsp conceptTreeRsp = new BasicConceptTreeRsp();
        if (concept == null) {
            return conceptTreeRsp;
        }
        conceptTreeRsp.setId(concept.getId());
        conceptTreeRsp.setImgUrl(concept.getImageUrl());
        conceptTreeRsp.setMeaningTag(concept.getMeaningTag());
        conceptTreeRsp.setName(concept.getName());
        conceptTreeRsp.setParentId(concept.getConceptId());
        conceptTreeRsp.setType(concept.getType());
        conceptTreeRsp.setChildren(Lists.newArrayList());
        return conceptTreeRsp;
    }

    private static void fillTree(List<BasicConceptTreeRsp> treeItemVoList, Map<Long, List<BasicConceptTreeRsp>> treeMap) {
        if (CollectionUtils.isEmpty(treeItemVoList)) {
            return;
        }
        for (BasicConceptTreeRsp treeItemVo : treeItemVoList) {
            List<BasicConceptTreeRsp> child = treeMap.getOrDefault(treeItemVo.getId(), new ArrayList<>());
            if (!CollectionUtils.isEmpty(child)) {
                treeItemVo.setChildren(child);
                fillTree(child, treeMap);
            }
        }
    }
}
