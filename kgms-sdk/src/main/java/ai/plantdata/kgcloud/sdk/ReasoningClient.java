package ai.plantdata.kgcloud.sdk;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.sdk.exection.client.ReasoningClientEx;
import ai.plantdata.kgcloud.sdk.req.ReasoningExecuteReq;
import ai.plantdata.kgcloud.sdk.req.app.sematic.ReasoningReq;
import ai.plantdata.kgcloud.sdk.rsp.app.RelationReasonRuleRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.semantic.GraphReasoningResultRsp;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/27 10:59
 */
@FeignClient(value = "kgms", path = "app/reasoning", contextId = "reasoningClient",fallback = ReasoningClientEx.class)
public interface ReasoningClient {


    /**
     * 推理规则执行
     *
     * @param reasoningExecuteReq
     * @return
     */
    @PostMapping("execute")
    ApiReturn<CommonBasicGraphExploreRsp> execute(@RequestBody ReasoningExecuteReq reasoningExecuteReq);



    /**
     * 推理规则生成
     *
     * @param reasonConfig
     * @return
     */
    @PostMapping("rule/generate")
    ApiReturn<List<RelationReasonRuleRsp>> reasoningRuleGenerate(@RequestBody Map<Long, Object> reasonConfig);


    /**
     * 推理
     *
     * @param kgName
     * @param var2
     * @return
     */
    @PostMapping("execute/{kgName}")
    ApiReturn<GraphReasoningResultRsp> reasoning(@ApiParam("图谱名称") @PathVariable("kgName") String kgName, @RequestBody ReasoningReq var2);
}

