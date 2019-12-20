package com.plantdata.kgcloud.domain.app.controller;

import ai.plantdata.kg.api.pub.SemanticApi;
import ai.plantdata.kg.api.pub.req.SemanticDistanceFrom;
import ai.plantdata.kg.api.semantic.QuestionAnswersApi;
import ai.plantdata.kg.api.semantic.ReasoningApi;
import ai.plantdata.kg.api.semantic.req.QueryReq;
import ai.plantdata.kg.api.semantic.req.ReasoningReq;
import ai.plantdata.kg.api.semantic.rsp.AnswerDataRsp;
import ai.plantdata.kg.api.semantic.rsp.ReasoningResultRsp;
import ai.plantdata.kg.common.bean.SemanticDistance;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.app.controller.module.SdkOpenApiInterface;
import com.plantdata.kgcloud.domain.app.converter.DistanceConverter;
import com.plantdata.kgcloud.domain.common.converter.RestCopyConverter;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.sdk.req.app.sematic.DistanceListReq;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.DistanceEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.GraphReasoningResultRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.QaAnswerDataRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/6 11:26
 */
@RestController
@RequestMapping("app/semantic")
public class SemanticController implements SdkOpenApiInterface {

    @Autowired
    private ReasoningApi reasoningApi;
    @Autowired
    private QuestionAnswersApi questionAnswersApi;
    @Autowired
    private SemanticApi semanticApi;

    @ApiOperation("意图图谱生成")
    @GetMapping("qa/init/{kgName}")
    public ApiReturn kbQaiInit(@ApiParam("图谱名称") @PathVariable("kgName") String kgName) {
        questionAnswersApi.create(kgName);
        return ApiReturn.success();
    }

    @ApiOperation("知识图谱问答")
    @GetMapping("qa/{kgName}")
    public ApiReturn<QaAnswerDataRsp> qaKbQa(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                             @RequestBody QueryReq queryReq) {
        RestResp<AnswerDataRsp> query = questionAnswersApi.query(kgName, queryReq);
        return ApiReturn.success(RestCopyConverter.copyRestRespResult(query, new QaAnswerDataRsp()));
    }

    @ApiOperation("隐含关系推理")
    @PostMapping("reasoning/execute/{kgName}")
    public ApiReturn<GraphReasoningResultRsp> reasoning(@ApiParam(value = "图谱名称") @PathVariable("kgName") String kgName,
                                                        @RequestBody ReasoningReq reasoningReq) {
        RestResp<ReasoningResultRsp> reasoning = reasoningApi.reasoning(kgName, reasoningReq);
        return ApiReturn.success(RestCopyConverter.copyRestRespResult(reasoning, new GraphReasoningResultRsp()));
    }

    @ApiOperation("两个实体间语义距离查询")
    @PostMapping("distance/score/{kgName}")
    public ApiReturn<Double> semanticDistanceScore(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                   @RequestParam("entityIdOne") Long entityIdOne, @RequestParam("entityIdTwo") Long entityIdTwo) {
        Optional<Double> distanceOpt = RestRespConverter.convert(semanticApi.distanceScore(kgName, entityIdOne, entityIdTwo));
        return ApiReturn.success(distanceOpt.orElse(NumberUtils.DOUBLE_ZERO));
    }

    @ApiOperation("实体语义相关实体查询")
    @PostMapping("distance/list/{kgName}")
    public ApiReturn<List<DistanceEntityRsp>> semanticDistanceRelevance(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                                        @Valid @RequestBody DistanceListReq listReq) {

        SemanticDistanceFrom distanceFrom = DistanceConverter.distanceListReqToSemanticDistanceFrom(listReq);
        Optional<List<SemanticDistance>> distanceOpt = RestRespConverter.convert(semanticApi.distance(kgName, distanceFrom));
        return ApiReturn.success();
    }
}
