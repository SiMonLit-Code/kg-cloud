package com.plantdata.kgcloud.domain.app.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphSemanticApplicationInterface;
import com.plantdata.kgcloud.sdk.SemanticClient;
import com.plantdata.kgcloud.sdk.req.app.sematic.QueryReq;
import com.plantdata.kgcloud.sdk.req.app.sematic.ReasoningReq;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.GraphReasoningResultRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.QaAnswerDataRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/14 15:58
 */
@RestController
@RequestMapping("app/semantic/kbqa")
public class GraphSemanticController implements GraphSemanticApplicationInterface {

    @Autowired
    private SemanticClient semanticClient;

    @ApiOperation("意图图谱生成")
    @GetMapping("init/{kgName}")
    public ApiReturn kbQaiInit(@ApiParam("图谱名称") @PathVariable String kgName) {
        semanticClient.create(kgName);
        return ApiReturn.success();
    }

    @ApiOperation("知识图谱问答")
    @GetMapping("{kgName}")
    public ApiReturn<QaAnswerDataRsp> qaKbQa(@ApiParam("图谱名称") @PathVariable String kgName,
                                             @RequestBody QueryReq queryReq) {

        return semanticClient.query(kgName, queryReq);
    }

    @ApiOperation("隐含关系推理")
    @PostMapping("/execute/{kgName}")
    public ApiReturn<GraphReasoningResultRsp> reasoning(@ApiParam(value = "图谱名称") @PathVariable("kgName") String kgName,
                                                        @RequestBody ReasoningReq reasoningReq) {
        return semanticClient.reasoning(kgName, reasoningReq);
    }
}
