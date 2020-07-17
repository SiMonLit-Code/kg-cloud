package ai.plantdata.kgcloud.sdk.exection.client;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.sdk.ReasoningClient;
import ai.plantdata.kgcloud.sdk.req.ReasoningExecuteReq;
import ai.plantdata.kgcloud.sdk.req.app.sematic.ReasoningReq;
import ai.plantdata.kgcloud.sdk.rsp.app.RelationReasonRuleRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.semantic.GraphReasoningResultRsp;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ReasoningClientEx implements ReasoningClient {

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
