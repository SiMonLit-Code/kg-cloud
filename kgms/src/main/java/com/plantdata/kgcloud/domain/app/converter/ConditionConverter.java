package com.plantdata.kgcloud.domain.app.converter;

import com.google.common.collect.Maps;
import com.plantdata.kgcloud.sdk.req.app.*;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/29 15:37
 */
public class ConditionConverter extends BasicConverter {

    public static Map<String, Map<String, Object>> relationAttrReqToMap(List<RelationAttrReq> attrReqList) {
        return attrReqList.stream().collect(Collectors.toMap(a -> buildEdgeFilter(a.getAttrId(), a.getSeqNo()),
                ConditionConverter::buildMongoQueryMap));
    }

    public static Map<String, Map<String, Object>> relationAttrReqToMapV1(List<RelationAttrReq> attrReqList) {
        return attrReqList.stream().collect(Collectors.toMap(a -> String.valueOf(a.getAttrId()),
                ConditionConverter::relationAttrReqToStringMap));
    }

    public static Map<String, Integer> relationAttrSortToMap(List<AttrSortReq> sortReqList) {
        return sortReqList.stream().collect(Collectors.toMap(a -> buildEdgeFilter(a.getAttrId(), a.getSeqNo()),
                AttrSortReq::getSort));
    }

    private static String buildEdgeFilter(Integer attrId, Integer seqNo) {
        return "attr_ext_" + attrId + "_" + seqNo;
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
        return entityScreeningList.stream().filter(a -> a.getAttrDefId() != null).collect(Collectors.toMap(EntityQueryFiltersReq::getAttrDefId,
                s -> s.get$eq() != null ? s.get$eq() : buildMongoQueryMap(s)));
    }

    public static Map<String, Object> entityListToMap(List<EntityQueryFiltersReq> entityScreeningList) {
        if (entityScreeningList == null) {
            return Collections.emptyMap();
        }
        return entityScreeningList.stream().filter(a -> a.getAttrDefId() != null).collect(Collectors.toMap(s -> String.valueOf(s.getAttrDefId()),
                s -> s.get$eq() != null ? s.get$eq() : buildMongoQueryMap(s)));
    }

    public static Map<String, Object> buildSearchMapByDataAttrReq(@NotNull List<DataAttrReq> dataAttrReqs) {
        Map<String, Object> queryMap = Maps.newHashMap();
        dataAttrReqs.forEach(a -> {
            queryMap.put(String.valueOf(a.getAttrDefId()), buildMongoQueryMap(a));
        });
        return queryMap;
    }

    static Map<Integer, Map<Integer, Object>> buildEdgeAttrSearchMap(List<RelationAttrReq> attrReqList) {
        return attrReqList.stream().collect(Collectors.toMap(RelationAttrReq::getAttrId,
                ConditionConverter::relationAttrReqToIntMap));
    }

    private static Map<String, Object> relationAttrReqToStringMap(RelationAttrReq attrReq) {
        Map<String, Object> seqMap = Maps.newHashMap();
        seqMap.put(String.valueOf(attrReq.getSeqNo()), attrReq.get$eq() != null ? attrReq.get$eq() : buildMongoQueryMap(attrReq));
        return seqMap;
    }

    private static Map<Integer, Object> relationAttrReqToIntMap(RelationAttrReq attrReq) {
        Map<Integer, Object> seqMap = Maps.newHashMap();
        seqMap.put(attrReq.getSeqNo(), buildMongoQueryMap(attrReq));
        return seqMap;
    }

    private static <T extends CompareFilterReq> Map<String, Object> buildMongoQueryMap(T filtersReq) {
        Map<String, Object> map = Maps.newHashMap();
        consumerIfNoNull(filtersReq.get$eq(), a -> map.put("$eq", a));
        consumerIfNoNull(filtersReq.get$ne(), a -> map.put("$ne", a));
        consumerIfNoNull(filtersReq.get$gt(), a -> map.put("$gt", a));
        consumerIfNoNull(filtersReq.get$gte(), a -> map.put("$gte", a));
        consumerIfNoNull(filtersReq.get$lte(), a -> map.put("$lte", a));
        consumerIfNoNull(filtersReq.get$in(), a -> map.put("$in", a));
        consumerIfNoNull(filtersReq.get$nin(), a -> map.put("$nin", a));
        return map;
    }

}
