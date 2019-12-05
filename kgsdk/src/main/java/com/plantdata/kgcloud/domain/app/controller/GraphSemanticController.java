package com.plantdata.kgcloud.domain.app.controller;

import ai.plantdata.kg.api.semantic.QuestionAnswersApi;
import ai.plantdata.kg.api.semantic.ReasoningApi;
import ai.plantdata.kg.api.semantic.req.QueryReq;
import ai.plantdata.kg.api.semantic.req.ReasoningReq;
import ai.plantdata.kg.api.semantic.rsp.AnswerDataRsp;
import ai.plantdata.kg.api.semantic.rsp.ReasoningResultRsp;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.common.module.GraphSemanticApplicationInterface;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/14 15:58
 */
@RestController
@RequestMapping("app/semantic/kbqa")
public class GraphSemanticController implements GraphSemanticApplicationInterface {

    @Autowired
    private ReasoningApi reasoningApi;
    @Autowired
    private QuestionAnswersApi questionAnswersApi;

    @ApiOperation("意图图谱生成")
    @GetMapping("init/{kgName}")
    public ApiReturn kbQaiInit(@ApiParam("图谱名称") @PathVariable String kgName) {
        questionAnswersApi.create(kgName);
        return ApiReturn.success();
    }

    @ApiOperation("知识图谱问答")
    @GetMapping("{kgName}")
    public ApiReturn<AnswerDataRsp> qaKbQa(@ApiParam("图谱名称") @PathVariable String kgName,
                                               @RequestBody QueryReq queryReq) {
        Optional<AnswerDataRsp> answerRsp = RestRespConverter.convert(questionAnswersApi.query(kgName, queryReq));
        return ApiReturn.success(answerRsp.orElse(new AnswerDataRsp()));
    }

    @ApiOperation("隐含关系推理")
    @PostMapping("/execute/{kgName}")
    public RestResp<ReasoningResultRsp> reasoning(@ApiParam(value = "图谱名称") @PathVariable("kgName") String kgName,
                                                  @RequestBody ReasoningReq reasoningReq) {
        return reasoningApi.reasoning(kgName, reasoningReq);
    }
}
