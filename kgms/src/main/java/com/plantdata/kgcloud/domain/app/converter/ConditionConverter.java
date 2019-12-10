package com.plantdata.kgcloud.domain.app.converter;

import com.google.common.collect.Maps;
import com.plantdata.kgcloud.sdk.req.app.AttrSortReq;
import com.plantdata.kgcloud.sdk.req.app.EntityQueryFiltersReq;
import com.plantdata.kgcloud.sdk.req.app.RelationAttrReq;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/29 15:37
 */
public class ConditionConverter {

    public static Map<String, Map<String, Object>> relationAttrReqToMap(List<RelationAttrReq> attrReqList) {
        return attrReqList.stream().collect(Collectors.toMap(a -> String.valueOf(a.getAttrId()), ConditionConverter::relationAttrReqToSeqMap));
    }

    public static Map<String, Integer> relationAttrSortToMap(List<AttrSortReq> sortReqList) {

        return sortReqList.stream().collect(Collectors.toMap(a -> "attr_ext" + a.getAttrId() + "_" + a.getSeqNo(), AttrSortReq::getSort));
    }


    private static Map<String, Object> relationAttrReqToSeqMap(RelationAttrReq attrReq) {

        Map<String, Object> seqMap = Maps.newHashMap();


        Map<String, Object> map = Maps.newHashMap();
        if (attrReq.getGt() != null) {
            map.put("$gt", attrReq.getGt());
        }
        if (attrReq.getLt() != null) {
            map.put("$lt", attrReq.getEq());
        }
        if (attrReq.getEq() != null) {
            map.put("$eq", attrReq.getEq());
        }
        seqMap.put(String.valueOf(attrReq.getSeqNo()), map);
        return seqMap;
    }


    public static List<Map<String, Object>> entityScreeningListToMap(List<EntityQueryFiltersReq> entityScreeningList) {
        if (CollectionUtils.isEmpty(entityScreeningList)) {
            return Collections.emptyList();
        }
        if (entityScreeningList.size() > 0) {
            entityScreeningList.get(0).setRelation(null);
        }
        List<List<EntityQueryFiltersReq>> lists = new ArrayList<>();
        int i = 0;
        for (EntityQueryFiltersReq one : entityScreeningList) {
            if (one.getRelation() != null && one.getRelation() == 0) {
                i++;
            }
            if (lists.size() <= i) {
                lists.add(i, new ArrayList<>());
            }
            lists.get(i).add(one);
        }
        return lists.stream().map(ConditionConverter::entityListToMap).collect(Collectors.toList());
    }

    public static Map<Integer, Object> entityListToIntegerKeyMap(List<EntityQueryFiltersReq> entityScreeningList) {
        if (entityScreeningList == null) {
            return Collections.emptyMap();
        }
        return entityScreeningList.stream().collect(Collectors.toMap(EntityQueryFiltersReq::getAttrId, s -> s.getEq() != null ? s.getEq() : buildRangeMap(s)));
    }

    public static Map<String, Object> entityListToMap(List<EntityQueryFiltersReq> entityScreeningList) {
        if (entityScreeningList == null) {
            return Collections.emptyMap();
        }
        return entityScreeningList.stream().collect(Collectors.toMap(s -> String.valueOf(s.getAttrId()), s -> s.getEq() != null ? s.getEq() : buildRangeMap(s)));
    }

    private static Map<String, Object> buildRangeMap(EntityQueryFiltersReq filtersReq) {
        Map<String, Object> map = Maps.newHashMap();
        if (filtersReq.getNe() != null) {
            map.put("$ne", filtersReq.getNe());
        }
        if (filtersReq.getGt() != null) {
            map.put("$gt", filtersReq.getGt());
        }
        if (filtersReq.getLt() != null) {
            map.put("$lt", filtersReq.getLt());
        }
        if (filtersReq.getGte() != null) {
            map.put("$gte", filtersReq.getGte());
        }
        if (filtersReq.getLte() != null) {
            map.put("$lte", filtersReq.getLte());
        }
        if (filtersReq.getIn() != null) {
            map.put("$in", filtersReq.getIn());
        }
        if (filtersReq.getNin() != null) {
            map.put("$nin", filtersReq.getNin());
        }
        return map;
    }
}
