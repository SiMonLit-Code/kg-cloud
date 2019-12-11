package com.plantdata.kgcloud.domain.app.bo;

import ai.plantdata.kg.api.pub.req.EntityRelationDegreeFrom;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByEntityIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.IdsFilterReq;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/10 17:56
 */
public class GraphRelationStatisticBO {


    public static EntityRelationDegreeFrom buildDegreeFrom(EdgeStatisticByEntityIdReq statisticReq) {
        Map<Integer, List<Integer>> allowAttrMap = GraphRelationStatisticBO.buildDataFilterMap(statisticReq.getAllowAttrs());
        Map<Integer, List<Long>> allowTypeMap = GraphRelationStatisticBO.buildDataFilterMap(statisticReq.getAllowTypes());
        EntityRelationDegreeFrom degreeFrom = new EntityRelationDegreeFrom();
        degreeFrom.setAllowAtts(allowAttrMap);
        degreeFrom.setAllowTypes(allowTypeMap);
        degreeFrom.setEntityId(statisticReq.getEntityId());
        degreeFrom.setDirection(1);
        degreeFrom.setIsDistinct(statisticReq.getIsDistinct() ? 1 : 0);
        return degreeFrom;
    }

    public static List<Map<String, Object>> graphDegreeMapToList(Map<Integer, Integer> outDegree, Map<Integer, Integer> inDegree, Map<Integer, Integer> centrality) {
        if (outDegree == null && inDegree == null) {
            return Collections.emptyList();
        }
        Map<Integer, Map<String, Object>> map = Maps.newHashMap();
        if (outDegree != null) {
            outDegree.forEach((k, v) -> {
                Map<String, Object> degree = Maps.newHashMap();
                degree.put("outDegree", v);
                map.put(k, degree);
            });
        }
        if (inDegree != null) {
            inDegree.forEach((k, v) -> {
                if (map.containsKey(k)) {
                    map.get(k).put("inDegree", v);
                } else {
                    Map<String, Object> degree = Maps.newHashMap();
                    degree.put("inDegree", v);
                    map.put(k, degree);
                }
            });
        }
        if (centrality != null) {
            centrality.forEach((k, v) -> {
                if (map.containsKey(k)) {
                    map.get(k).put("degree", v);
                } else {
                    Map<String, Object> degree = Maps.newHashMap();
                    degree.put("degree", v);
                    map.put(k, degree);
                }
            });
        }
        return map.entrySet().stream().map(v -> {
            Map<String, Object> data = v.getValue();
            data.put("layer", v.getKey());
            if (!data.containsKey("outDegree")) {
                data.put("outDegree", 0);
            }
            if (!data.containsKey("inDegree")) {
                data.put("inDegree", 0);
            }
            if (!data.containsKey("degree")) {
                data.put("degree", 0);
            }
            return data;
        }).collect(Collectors.toList());
    }

    private static <T> Map<Integer, List<T>> buildDataFilterMap(List<IdsFilterReq<T>> idsFilterReqs) {
        return CollectionUtils.isEmpty(idsFilterReqs) ? Collections.emptyMap() : idsFilterReqs.stream().collect(Collectors.toMap(IdsFilterReq::getLayer, IdsFilterReq::getIds, (oldData, newData) -> oldData));
    }

//
//    public static Object EntityScreeningNameListToStatData(List<ResultsBean> stataData, int returnType) {
//        if (returnType == StatDataBean.returnType.KV) {
//            return stataData.stream().map(s -> {
//                Map<String, Object> map = new HashMap<>();
//                map.put("name", s.getName());
//                map.put("value", s.getTotal());
//                if (s.getRelation() != null && !s.getRelation().isEmpty())
//                    map.put("ids", s.getRelation());
//                if (s.getEntity() != null && !s.getEntity().isEmpty())
//                    map.put("ids", s.getEntity());
//                return map;
//            }).collect(Collectors.toList());
//        }
//        StatDataBean statDataBean = new StatDataBean();
//        if (stataData == null) {
//            return statDataBean;
//        }
//        List<String> xdata = new ArrayList<>();
//        List<Long> sdata = new ArrayList<>();
//
//        for (ResultsBean one : stataData) {
//            Long value = one.getTotal();
//            String name = one.getName();
//            xdata.add(name);
//            sdata.add(value);
//        }
//
//        statDataBean.addData2X(xdata);
//        statDataBean.addData2Series("", sdata);
//        return statDataBean;
//    }
}
