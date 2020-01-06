package com.plantdata.kgcloud.domain.app.controller;

import ai.plantdata.kg.api.semantic.ReasoningApi;
import ai.plantdata.kg.api.semantic.req.ReasoningReq;
import ai.plantdata.kg.api.semantic.rsp.ReasoningResultRsp;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.app.controller.module.SdkOpenApiInterface;
import com.plantdata.kgcloud.domain.app.service.RuleReasoningService;
import com.plantdata.kgcloud.domain.common.converter.RestCopyConverter;
import com.plantdata.kgcloud.sdk.rsp.app.RelationReasonRuleRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.GraphReasoningResultRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/27 10:54
 */
@RestController
@RequestMapping("app/reasoning")
public class ReasonController implements SdkOpenApiInterface {

    @Autowired
    private ReasoningApi reasoningApi;
    @Autowired
    private RuleReasoningService ruleReasoningService;

    @ApiOperation("隐含关系推理")
    @PostMapping("execute/{kgName}")
    public ApiReturn<GraphReasoningResultRsp> reasoning(@ApiParam(value = "图谱名称") @PathVariable("kgName") String kgName,
                                                        @RequestBody ReasoningReq reasoningReq) {
        RestResp<ReasoningResultRsp> reasoning = reasoningApi.reasoning(kgName, reasoningReq);
        return ApiReturn.success(RestCopyConverter.copyRestRespResult(reasoning, new GraphReasoningResultRsp()));
    }

    @ApiOperation("推理规则生成")
    @PostMapping("rule/generate")
    public ApiReturn<List<RelationReasonRuleRsp>> reasoningRuleGenerate(Map<Long, Object> reasonConfig) {
        List<RelationReasonRuleRsp> reasonRuleRspList = ruleReasoningService.generateReasoningRule(reasonConfig);
        return ApiReturn.success(reasonRuleRspList);
    }

}
