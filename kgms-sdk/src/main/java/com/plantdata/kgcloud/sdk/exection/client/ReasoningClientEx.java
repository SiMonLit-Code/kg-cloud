package com.plantdata.kgcloud.sdk.exection.client;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.ReasoningClient;
import com.plantdata.kgcloud.sdk.req.ReasoningExecuteReq;
import com.plantdata.kgcloud.sdk.req.ReasoningQueryReq;
import com.plantdata.kgcloud.sdk.req.app.sematic.ReasoningReq;
import com.plantdata.kgcloud.sdk.rsp.ReasoningRsp;
import com.plantdata.kgcloud.sdk.rsp.app.RelationReasonRuleRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.GraphReasoningResultRsp;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-19 19:20
 **/
@Component
public class ReasoningClientEx implements ReasoningClient {
    @Override
    public ApiReturn<Page<ReasoningRsp>> list(ReasoningQueryReq reasoningQueryReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<CommonBasicGraphExploreRsp> execute(ReasoningExecuteReq reasoningExecuteReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<RelationReasonRuleRsp>> reasoningRuleGenerate(Map<Long, Object> reasonConfig) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<GraphReasoningResultRsp> reasoning(String kgName, ReasoningReq var2) {
        return ApiReturn.fail(500,"请求超时");
    }
}
