package com.plantdata.kgcloud.plantdata.controller;


import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.converter.semantic.QaConverter;
import com.plantdata.kgcloud.plantdata.converter.semantic.ReasonConverter;
import com.plantdata.kgcloud.plantdata.req.reason.InferenceParameter;
import com.plantdata.kgcloud.plantdata.req.semantic.QaKbqaParameter;
import com.plantdata.kgcloud.sdk.ReasoningClient;
import com.plantdata.kgcloud.sdk.SemanticClient;
import com.plantdata.kgcloud.sdk.req.app.sematic.QueryReq;
import com.plantdata.kgcloud.sdk.req.app.sematic.ReasoningReq;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.GraphReasoningResultRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.QaAnswerDataRsp;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("sdk/semantic")
public class SemanticController implements SdkOldApiInterface {

    @Autowired
    private SemanticClient semanticClient;
    @Autowired
    private ReasoningClient reasoningClient;

    @ApiOperation("意图图谱生成")
    @GetMapping("kbqa/init")
    public RestResp kbqaInit(@ApiParam(required = true) @RequestParam("kgName") String kgName) {
        semanticClient.create(kgName);
        return new RestResp();
    }


    @ApiOperation("知识图谱问答")
    @PostMapping("kbqa")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "query", required = true, dataType = "string", paramType = "form", value = "查询问题"),
            @ApiImplicitParam(name = "pageModel", dataType = "string", paramType = "form", value = "pageModel")
    })
    public RestResp<QaAnswerDataRsp> qaKbqa(@Valid @ApiIgnore QaKbqaParameter param) {

        Function<QueryReq, ApiReturn<QaAnswerDataRsp>> returnFunction = a -> semanticClient.qaKbQa(param.getKgName(), a);
        Optional<QaAnswerDataRsp> dataRsp = returnFunction
                .compose(QaConverter::qaKbqaParameterToQueryReq)
                .andThen(BasicConverter::apiReturnData)
                .apply(param);
        return new RestResp<>(dataRsp.orElse(new QaAnswerDataRsp()));
    }

    @ApiOperation("隐含关系推理")
    @PostMapping("reasoning")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "ids", required = true, dataType = "array", paramType = "form", value = "推理的实例ids[71]"),
            @ApiImplicitParam(name = "ruleConfig", required = true, dataType = "string", paramType = "form", value = "推理规则"),
            @ApiImplicitParam(name = "pos", required = true, dataType = "string", paramType = "form", value = "页数"),
            @ApiImplicitParam(name = "size", dataType = "string", paramType = "form", value = "定义域"),
    })
    public RestResp<GraphReasoningResultRsp> inference(@RequestParam("kgName")String kgName,@Valid @ApiIgnore ReasoningReq reasoningReq) {
        ApiReturn<GraphReasoningResultRsp> reasoning = reasoningClient.reasoning(kgName,reasoningReq);
        return new RestResp<>(BasicConverter.apiReturnData(reasoning).orElse(new GraphReasoningResultRsp()));
    }


}
