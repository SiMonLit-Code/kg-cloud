package com.plantdata.kgcloud.domain.app.controller;

import ai.plantdata.kg.api.pub.SemanticApi;
import ai.plantdata.kg.api.pub.req.SemanticDistanceFrom;
import ai.plantdata.kg.api.semantic.QuestionAnswersApi;
import ai.plantdata.kg.api.semantic.ReasoningApi;
import ai.plantdata.kg.api.semantic.req.QueryReq;
import ai.plantdata.kg.api.semantic.rsp.AnswerDataRsp;
import ai.plantdata.kg.api.semantic.rsp.IntentDataBean;
import ai.plantdata.kg.common.bean.SemanticDistance;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.app.controller.module.SdkOpenApiInterface;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.app.converter.DistanceConverter;
import com.plantdata.kgcloud.domain.app.converter.SegmentConverter;
import com.plantdata.kgcloud.domain.common.converter.RestCopyConverter;
import com.plantdata.kgcloud.domain.common.util.KGUtil;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.sdk.req.app.sematic.DistanceListReq;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.DistanceEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.IntentDataBeanRsp;
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
        questionAnswersApi.create(kgName,KGUtil.dbName(kgName));
        return ApiReturn.success();
    }

    @PostMapping({"qa/intent"})
    public ApiReturn<IntentDataBeanRsp> intent(@ApiParam("图谱名称") @RequestParam("kgName") String kgName,
                                               @ApiParam("自然语言输入") @RequestParam("query") String query,
                                               @RequestParam(value = "size", defaultValue = "5") int size) {
        Optional<IntentDataBean> dataBean = RestRespConverter.convert(questionAnswersApi.intent(kgName,KGUtil.dbName(kgName), query, size));
        if (!dataBean.isPresent()) {
            return ApiReturn.success(new IntentDataBeanRsp());
        }
        IntentDataBeanRsp beanRsp = BasicConverter.copy(dataBean.get(), IntentDataBeanRsp.class);
        return ApiReturn.success(beanRsp);
    }

    @ApiOperation("知识图谱问答")
    @PostMapping("qa/{kgName}")
    public ApiReturn<QaAnswerDataRsp> qaKbQa(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                             @RequestBody QueryReq queryReq) {
        Optional<AnswerDataRsp> query = RestRespConverter.convert(questionAnswersApi.query(kgName,KGUtil.dbName(kgName), queryReq));
        if (!query.isPresent()) {
            return ApiReturn.success(new QaAnswerDataRsp());
        }
        QaAnswerDataRsp beanRsp = SegmentConverter.AnswerDataRspToQaAnswerDataRsp(query.get());
        return ApiReturn.success(beanRsp);
    }

    @ApiOperation("两个实体间语义距离查询")
    @PostMapping("distance/score/{kgName}")
    public ApiReturn<Double> semanticDistanceScore(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                   @RequestParam("entityIdOne") Long entityIdOne, @RequestParam("entityIdTwo") Long entityIdTwo) {
        Optional<Double> distanceOpt = RestRespConverter.convert(semanticApi.distanceScore(KGUtil.dbName(kgName), entityIdOne, entityIdTwo));
        return ApiReturn.success(distanceOpt.orElse(NumberUtils.DOUBLE_ZERO));
    }

}
