package com.plantdata.kgcloud.domain.app.controller;

import ai.plantdata.kg.api.semantic.ReasoningApi;
import ai.plantdata.kg.api.semantic.rsp.ReasoningResultRsp;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.domain.app.controller.module.SdkOpenApiInterface;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.app.service.RuleReasoningService;
import com.plantdata.kgcloud.domain.common.converter.ApiReturnConverter;
import com.plantdata.kgcloud.domain.common.util.KGUtil;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.ReasoningClient;
import com.plantdata.kgcloud.sdk.req.app.sematic.ReasoningReq;
import com.plantdata.kgcloud.sdk.rsp.app.RelationReasonRuleRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.GraphReasoningResultRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/27 10:54
 */
@RestController
@RequestMapping("app/reasoning")
@Slf4j
public class ReasonController implements SdkOpenApiInterface {

    @Autowired
    private ReasoningApi reasoningApi;
    @Autowired
    private RuleReasoningService ruleReasoningService;

    @ApiOperation("隐含关系推理")
    @PostMapping("execute/{kgName}")
    public ApiReturn<GraphReasoningResultRsp> reasoning(@ApiParam(value = "图谱名称") @PathVariable("kgName") String kgName,
                                                        @RequestBody ReasoningReq reasoningReq) {
        ai.plantdata.kg.api.semantic.req.ReasoningReq req = BasicConverter.copy(reasoningReq, ai.plantdata.kg.api.semantic.req.ReasoningReq.class);
        Optional<ReasoningResultRsp> reasonOpt;
        try {
            reasonOpt = RestRespConverter.convert(reasoningApi.reasoning(KGUtil.dbName(kgName), req));
        } catch (Exception e) {
            log.error("reasonRule:{}", reasoningReq.getRuleConfig());
            throw BizException.of(AppErrorCodeEnum.REASON_RULE_ERROR);
        }
        return ApiReturn.success(reasonOpt.isPresent()
                ? BasicConverter.copy(reasonOpt.get(), GraphReasoningResultRsp.class)
                : new GraphReasoningResultRsp());
    }

    @ApiOperation("推理规则生成")
    @PostMapping("rule/generate")
    public ApiReturn<List<RelationReasonRuleRsp>> reasoningRuleGenerate(Map<Long, Object> reasonConfig) {
        List<RelationReasonRuleRsp> reasonRuleRspList = ruleReasoningService.generateReasoningRule(reasonConfig);
        return ApiReturn.success(reasonRuleRspList);
    }

}
