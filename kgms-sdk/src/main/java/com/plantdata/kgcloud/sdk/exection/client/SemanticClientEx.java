package com.plantdata.kgcloud.sdk.exection.client;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.SemanticClient;
import com.plantdata.kgcloud.sdk.req.app.sematic.GremlinReq;
import com.plantdata.kgcloud.sdk.req.app.sematic.NerSearchReq;
import com.plantdata.kgcloud.sdk.req.app.sematic.QueryReq;
import com.plantdata.kgcloud.sdk.rsp.app.GremlinRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.IntentDataBeanRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.QaAnswerDataRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.SemanticSegWordRsp;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-19 19:20
 **/
@Component
public class SemanticClientEx implements SemanticClient {
    @Override
    public ApiReturn<QaAnswerDataRsp> qaKbQa(String kgName, QueryReq queryReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<GremlinRsp> gremlinQuery(String kgName, GremlinReq req) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn create(String kgName) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<SemanticSegWordRsp>> ner(NerSearchReq var1) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<IntentDataBeanRsp> intent(String kgName, String query, int size) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<Double> semanticDistanceScore(String kgName, Long entityIdOne, Long entityIdTwo) {
        return ApiReturn.fail(500,"请求超时");
    }
}
