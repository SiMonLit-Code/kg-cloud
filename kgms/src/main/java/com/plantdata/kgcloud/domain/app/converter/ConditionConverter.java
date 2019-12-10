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

    public static Map<String, Object> entityListToMap(List<EntityQueryFiltersReq> entityScreeningList) {
        if (entityScreeningList == null) {
            return Collections.emptyMap();
        }
        return entityScreeningList.stream().collect(Collectors.toMap(s -> String.valueOf(s.getAttrId()), s -> {

            if (s.getEq() != null) {
                return s.getEq();
            }
            Map<String, Object> map = new HashMap<>();
            if (s.getNe() != null) {
                map.put("$ne", s.getNe());
            }
            if (s.getGt() != null) {
                map.put("$gt", s.getGt());
            }
            if (s.getLt() != null) {
                map.put("$lt", s.getLt());
            }
            if (s.getGte() != null) {
                map.put("$gte", s.getGte());
            }
            if (s.getLte() != null) {
                map.put("$lte", s.getLte());
            }
            if (s.getIn() != null) {
                map.put("$in", s.getIn());
            }
            if (s.getNin() != null) {
                map.put("$nin", s.getNin());
            }
            return map;
        }));
    }
}
