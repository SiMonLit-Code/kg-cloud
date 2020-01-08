package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.pub.req.MongoQueryFrom;
import ai.plantdata.kg.api.pub.resp.EntityVO;
import ai.plantdata.kg.common.bean.BasicInfo;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.constant.EsKwConstants;
import com.plantdata.kgcloud.domain.app.converter.graph.GraphCommonConverter;
import com.plantdata.kgcloud.domain.app.dto.CoordinatesDTO;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.sdk.req.app.ComplexGraphVisualReq;
import com.plantdata.kgcloud.sdk.rsp.app.ComplexGraphVisualRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CoordinateReq;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/23 10:15
 */
public class ComplexGraphAnalysisConverter extends BasicConverter {


    public static MongoQueryFrom complexGraphVisualReqReqToMongoQueryFrom(String kgName, ComplexGraphVisualReq analysisReq) {
        MongoQueryFrom queryFrom = new MongoQueryFrom();
        queryFrom.setCollection(EsKwConstants.DEGREE);
        queryFrom.setQuery(buildComplexGraphVisualAggQueryList(analysisReq.getType(), analysisReq.getSize()));
        queryFrom.setKgName((kgName + "_data_" + analysisReq.getAzkId()));
        JsonUtils.objToJson(queryFrom);
        return queryFrom;
    }

    public static CoordinatesDTO mapToCoordinatesDTO(@NonNull Map<String, Object> resMap) {
        CoordinatesDTO coor = new CoordinatesDTO();
        Object id = resMap.get("id");
        Object x = resMap.get("x");
        Object y = resMap.get("y");
        Object lou = resMap.get(EsKwConstants.LOUVAIN);
        Object distance = resMap.get(EsKwConstants.DISTANCE);
        consumerIfNoNull(id, a -> coor.setId(Long.valueOf(id.toString())));
        consumerIfNoNull(x, a -> coor.setX((Double) x));
        consumerIfNoNull(y, a -> coor.setY((Double) y));
        consumerIfNoNull(lou, a -> coor.setCluster(Long.valueOf(lou.toString())));
        consumerIfNoNull(distance, a -> coor.setDistance((Double) distance));
        return coor;
    }

    public static ComplexGraphVisualRsp.CoordinatesEntityRsp entityVoToCoordinatesEntityRsp(@NonNull EntityVO entity, Map<Long, BasicInfo> conceptIdMap, CoordinatesDTO coordinates) {
        ComplexGraphVisualRsp.CoordinatesEntityRsp entityRsp = EntityConverter.entityVoToBasicEntityRsp(entity, new ComplexGraphVisualRsp.CoordinatesEntityRsp());
        consumerIfNoNull(coordinates, a -> {
            entityRsp.setCluster(a.getCluster());
            entityRsp.setCoordinates(new CoordinateReq(a.getX(), a.getY()));
            entityRsp.setDistance(a.getDistance());
        });
        GraphCommonConverter.fillConcept(entityRsp.getConceptId(), entityRsp, conceptIdMap);
        return entityRsp;
    }

    private static List<Map<String, Object>> buildComplexGraphVisualAggQueryList(String type, Integer size) {
        List<Map<String, Object>> aggregate = new ArrayList<>();
        Map<String, Object> match = Maps.newHashMap();
        Map<String, Object> map = Maps.newHashMap();
        Map<String, Object> ne = Maps.newHashMap();
        ne.put("$ne", null);
        map.put("x", ne);
        match.put("$match", map);
        aggregate.add(match);


        map = Maps.newHashMap();
        Map<String, Object> sort = Maps.newHashMap();
        if (type.equals(EsKwConstants.LOUVAIN)) {
            map.put(EsKwConstants.FR + ".distance", 1);
        } else {
            map.put(type, -1);
        }
        sort.put("$sort", map);
        aggregate.add(sort);
        Map<String, Object> limit = Maps.newHashMap();
        limit.put("$limit", size);
        aggregate.add(limit);
        Map<String, Object> project = Maps.newHashMap();
        map = Maps.newHashMap();
        map.put("x", 1);
        map.put(EsKwConstants.LOUVAIN, 1);
        map.put("y", 1);
        map.put("id", 1);
        map.put(EsKwConstants.DISTANCE, 1);
        project.put("$project", map);
        aggregate.add(project);
        return aggregate;
    }
}
