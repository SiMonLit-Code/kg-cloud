package com.plantdata.kgcloud.domain.app.bo;

import ai.plantdata.kg.api.pub.req.EntityRelationDegreeFrom;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByEntityIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.IdsFilterReq;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.EdgeStatisticByEntityIdRsp;
import org.apache.commons.lang3.math.NumberUtils;
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
        EntityRelationDegreeFrom degreeFrom = new EntityRelationDegreeFrom();
        BasicConverter.consumerIfNoNull(statisticReq.getAllowAttrDefIds(),a->degreeFrom.setAllowAtts(GraphRelationStatisticBO.buildDataFilterMap(a)));
        BasicConverter.consumerIfNoNull(statisticReq.getAllowConceptIds(),a->  degreeFrom.setAllowTypes(GraphRelationStatisticBO.buildDataFilterMap(a)));
        degreeFrom.setEntityId(statisticReq.getEntityId());
        degreeFrom.setDistance(NumberUtils.INTEGER_ONE);
        degreeFrom.setIsDistinct(statisticReq.isDistinct() ? 1 : 0);
        return degreeFrom;
    }

    public static List<EdgeStatisticByEntityIdRsp> graphDegreeMapToList(Map<Integer, Integer> outDegree, Map<Integer, Integer> inDegree) {
        if (outDegree == null && inDegree == null) {
            return Collections.emptyList();
        }
        Map<Integer, EdgeStatisticByEntityIdRsp> map = Maps.newHashMap();
        if (outDegree != null) {
            outDegree.forEach((k, v) -> {
                EdgeStatisticByEntityIdRsp rsp = new EdgeStatisticByEntityIdRsp(k);
                rsp.setOutDegree(v);
                map.put(k, rsp);
            });
        }
        if (inDegree != null) {
            inDegree.forEach((k, v) -> {
                EdgeStatisticByEntityIdRsp rsp = map.getOrDefault(k, new EdgeStatisticByEntityIdRsp(k));
                rsp.setInDegree(v);
                map.put(k, rsp);
            });
        }
        if (CollectionUtils.isEmpty(map)) {
            map.forEach((k,v)->v.setDegree(v.getInDegree()+v.getOutDegree()));
        }
        return Lists.newArrayList(map.values());
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
