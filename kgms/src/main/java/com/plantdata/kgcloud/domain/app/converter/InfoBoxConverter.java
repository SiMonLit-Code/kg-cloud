package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.pub.req.FilterRelationFrom;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.sdk.req.app.RelationAttrReq;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonEntityRsp;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/10 13:58
 */
public class InfoBoxConverter {

    public static FilterRelationFrom reqToFilterRelationFrom(BasicGraphExploreRsp rsp, List<RelationAttrReq> attrReqList, List<RelationAttrReq> revserdAttrReqList) {
        FilterRelationFrom relationFrom = new FilterRelationFrom();
        if (!CollectionUtils.isEmpty(rsp.getRelationList())) {
            List<String> relationIdSet = Lists.newArrayList();
            List<Integer> attrIds = Lists.newArrayList();
            rsp.getRelationList().forEach(a -> {
                relationIdSet.add(a.getId());
                attrIds.add(a.getAttId());
            });
            relationFrom.setRelationIds(relationIdSet);
            relationFrom.setAttrIds(attrIds);
        }
        if (!CollectionUtils.isEmpty(rsp.getEntityList())) {
            List<Long> entityIdSet = rsp.getEntityList().stream().filter(a -> a.getConceptId() != null).map(CommonEntityRsp::getId).collect(Collectors.toList());
            relationFrom.setEntityIds(entityIdSet);
        }
        if (!CollectionUtils.isEmpty(attrReqList)) {
            relationFrom.setRelationAttrFilters(ConditionConverter.relationAttrReqToMap(attrReqList));
        }
        if (!CollectionUtils.isEmpty(revserdAttrReqList)) {
            relationFrom.setMetaFilters(Maps.newHashMap(ConditionConverter.relationAttrReqToMap(revserdAttrReqList)));
        }
        return relationFrom;
    }
}
