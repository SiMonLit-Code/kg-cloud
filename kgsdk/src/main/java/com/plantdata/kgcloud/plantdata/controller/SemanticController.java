package com.plantdata.kgcloud.plantdata.controller;


import cn.hiboot.mcn.core.model.result.RestResp;
import com.hiekn.pddocument.bean.PdDocument;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.converter.nlp.NlpConverter;
import com.plantdata.kgcloud.plantdata.converter.nlp.NlpConverter2;
import com.plantdata.kgcloud.plantdata.converter.semantic.QaConverter;
import com.plantdata.kgcloud.plantdata.converter.semantic.ReasonConverter;
import com.plantdata.kgcloud.plantdata.req.nlp.AnnotationParameter;
import com.plantdata.kgcloud.plantdata.req.reason.InferenceParameter;
import com.plantdata.kgcloud.plantdata.req.semantic.QaKbqaParameter;
import com.plantdata.kgcloud.sdk.NlpClient;
import com.plantdata.kgcloud.sdk.ReasoningClient;
import com.plantdata.kgcloud.sdk.SemanticClient;
import com.plantdata.kgcloud.sdk.req.app.nlp.EntityLinkingReq;
import com.plantdata.kgcloud.sdk.req.app.sematic.QueryReq;
import com.plantdata.kgcloud.sdk.req.app.sematic.ReasoningReq;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.TaggingItemRsp;
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
import java.util.Collections;
import java.util.List;
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
    @Autowired
    public NlpClient nlpClient;

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

    @ApiOperation("文本语义标注")
    @PostMapping("annotation")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", dataType = "string", paramType = "query", value = "kgName"),
            @ApiImplicitParam(name = "text", dataType = "string", paramType = "form", value = "待标注文本"),
            @ApiImplicitParam(name = "conceptIds", dataType = "string", paramType = "form", value = "标注范围，格式为json数组格式的概念列表"),
    })
    public RestResp<PdDocument> annotation(@Valid @ApiIgnore AnnotationParameter param) {
        Function<EntityLinkingReq, ApiReturn<List<TaggingItemRsp>>> returnFunction = a -> nlpClient.tagging(param.getKgName(), a);
        Optional<List<TaggingItemRsp>> opt = returnFunction
                .compose(NlpConverter::annotationParameterToEntityLinkingReq)
                .andThen(BasicConverter::apiReturnData)
                .apply(param);
        PdDocument document = NlpConverter2.annotationToPdDocument(opt.orElse(Collections.emptyList()));
        return new RestResp<>(document);
    }
}
