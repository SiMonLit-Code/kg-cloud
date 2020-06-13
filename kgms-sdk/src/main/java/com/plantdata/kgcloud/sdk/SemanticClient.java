

package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.req.app.sematic.DistanceListReq;
import com.plantdata.kgcloud.sdk.req.app.sematic.GremlinReq;
import com.plantdata.kgcloud.sdk.req.app.sematic.NerSearchReq;
import com.plantdata.kgcloud.sdk.req.app.sematic.QueryReq;
import com.plantdata.kgcloud.sdk.rsp.app.GremlinRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.DistanceEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.IntentDataBeanRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.QaAnswerDataRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.SemanticSegWordRsp;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Administrator
 */
@FeignClient(value = "kgms", path = "app/semantic", contextId = "semanticClient")
public interface SemanticClient {
    /**
     * 问答
     *
     * @param kgName
     * @param queryReq
     * @return
     */
    @PostMapping("qa/{kgName}")
    ApiReturn<QaAnswerDataRsp> qaKbQa(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                      @RequestBody QueryReq queryReq);

    /**
     * gremlin查询
     * @param kgName
     * @param gremlinQuery
     * @return
     */
    @PostMapping("gremlin/query/{kgName}")
    ApiReturn<GremlinRsp> gremlinQuery(@PathVariable("kgName") String kgName, @RequestBody GremlinReq req);

    /**
     * 初始化意图
     *
     * @param kgName
     * @return
     */
    @PostMapping("qa/init/{kgName}")
    ApiReturn create(@PathVariable("kgName") String kgName);

    /**
     * 实体识别
     *
     * @param var1
     * @return
     */
    @PostMapping({"qa/ner"})
    ApiReturn<List<SemanticSegWordRsp>> ner(NerSearchReq var1);

    /**
     * 语义识别
     *
     * @param kgName String
     * @param query  String
     * @param size   int
     * @return IntentDataBeanRsp
     */
    @PostMapping({"qa/intent"})
    ApiReturn<IntentDataBeanRsp> intent(@ApiParam("图谱名称") @RequestParam("kgName") String kgName,
                                        @ApiParam("自然语言输入") @RequestParam("query") String query,
                                        @RequestParam(value = "size", defaultValue = "5") int size);

    /**
     * 两个实体间语义距离查询
     *
     * @param kgName
     * @param entityIdOne
     * @param entityIdTwo
     * @return
     */
    @PostMapping("distance/score/{kgName}")
    ApiReturn<Double> semanticDistanceScore(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                            @RequestParam("entityIdOne") Long entityIdOne, @RequestParam("entityIdTwo") Long entityIdTwo);

}
