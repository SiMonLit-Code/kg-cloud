package com.plantdata.kgcloud.sdk.exection.client;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.sdk.KgDataClient;
import com.plantdata.kgcloud.sdk.req.app.EntityQueryWithConditionReq;
import com.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import com.plantdata.kgcloud.sdk.req.app.SparQlReq;
import com.plantdata.kgcloud.sdk.req.app.TraceabilityQueryReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.*;
import com.plantdata.kgcloud.sdk.req.edit.ConceptAddReq;
import com.plantdata.kgcloud.sdk.rsp.app.sparql.QueryResultRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionRsp;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-19 19:18
 **/
@Component
public class KgDataClientEx implements KgDataClient {
    @Override
    public ApiReturn<List<Map<String, Object>>> statisticCountEdgeByEntity(String kgName, EdgeStatisticByEntityIdReq statisticReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<Object> statisticEntityGroupByConcept(String kgName, EntityStatisticGroupByConceptReq statisticReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<Object> statisticAttrGroupByConcept(String kgName, EntityStatisticGroupByAttrIdReq statisticReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<Object> statisticRelation(String kgName, EdgeStatisticByConceptIdReq statisticReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<Object> statEdgeGroupByEdgeValue(String kgName, EdgeAttrStatisticByAttrValueReq statisticReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<Long> createConcept(String kgName, ConceptAddReq conceptAddReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<AttrDefinitionRsp>> searchAttrDefByConcept(String kgName, Long conceptId, String conceptKey, boolean inherit) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<QueryResultRsp> sparQlQuery(String kgName, SparQlReq sparQlReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<OpenEntityRsp>> queryEntityByNameAndMeaningTag(String kgName, List<EntityQueryWithConditionReq> conditionReqList) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<BasePage<OpenEntityRsp>> queryEntityBySource(String kgName, TraceabilityQueryReq req) {
        return ApiReturn.fail(500,"请求超时");
    }
}
