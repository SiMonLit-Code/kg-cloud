package com.plantdata.kgcloud.domain.app.controller;

import ai.plantdata.kg.api.pub.SemanticApi;
import ai.plantdata.kg.api.semantic.QuestionAnswersApi;
import ai.plantdata.kg.api.semantic.req.QueryReq;
import ai.plantdata.kg.api.semantic.rsp.AnswerDataRsp;
import ai.plantdata.kg.api.semantic.rsp.IntentDataBean;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.app.controller.module.SdkOpenApiInterface;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.app.converter.SegmentConverter;
import com.plantdata.kgcloud.domain.common.util.KGUtil;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.sdk.req.app.sematic.GremlinReq;
import com.plantdata.kgcloud.sdk.rsp.app.GremlinRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.IntentDataBeanRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.QaAnswerDataRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private QuestionAnswersApi questionAnswersApi;
    @Autowired
    private SemanticApi semanticApi;

    //    @Autowired
//    private QlApi qlApi;

    @ApiOperation("gremlin查询")
    @PostMapping("gremlin/query/{kgName}")
    public ApiReturn<GremlinRsp> gremlinQuery(@PathVariable("kgName") String kgName, @RequestBody GremlinReq req) {

        /*Gremlin gremlin = new Gremlin();
        gremlin.setKgName(KGUtil.dbName(kgName));
        gremlin.setGremlin(gremlinQuery);
        Optional<ResultSet> resOpt = RestRespConverter.convert(qlApi.gremlin(gremlin));
        if (!resOpt.isPresent()) {
            return ApiReturn.success(new GremlinRsp());
        }
        GremlinRsp copy = BasicConverter.copy(resOpt.get(), GremlinRsp.class);
        return ApiReturn.success(copy);*/
        return ApiReturn.success();
    }


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
        query = query.replaceAll("&ldquo;","“");
        query = query.replaceAll("&rdquo;","”");
        query = query.replaceAll("&quot;","\"");
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
        RestResp<Double> a = semanticApi.distanceScore(KGUtil.dbName(kgName), entityIdOne, entityIdTwo);
        Optional<Double> distanceOpt = RestRespConverter.convert(semanticApi.distanceScore(KGUtil.dbName(kgName), entityIdOne, entityIdTwo));
        return ApiReturn.success(distanceOpt.orElse(NumberUtils.DOUBLE_ZERO));
    }



}
