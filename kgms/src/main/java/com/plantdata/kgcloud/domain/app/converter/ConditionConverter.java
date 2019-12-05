package com.plantdata.kgcloud.domain.app.converter;

import com.plantdata.kgcloud.sdk.req.app.EntityQueryFiltersReq;
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

    private static Map<String, Object> entityListToMap(List<EntityQueryFiltersReq> entityScreeningList) {
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
