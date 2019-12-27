package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.req.app.sematic.ReasoningReq;
import com.plantdata.kgcloud.sdk.rsp.app.RelationReasonRuleRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.GraphReasoningResultRsp;
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
@FeignClient(value = "kgms", path = "app/reasoning", contextId = "reasoningClient")
public interface ReasoningClient {

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
