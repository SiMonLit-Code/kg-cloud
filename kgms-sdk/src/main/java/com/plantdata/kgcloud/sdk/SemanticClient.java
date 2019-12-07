

package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.req.app.sematic.NerSearchReq;
import com.plantdata.kgcloud.sdk.req.app.sematic.QueryReq;
import com.plantdata.kgcloud.sdk.req.app.sematic.ReasoningReq;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.GraphReasoningResultRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.IntentDataBean;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.QaAnswerDataRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.SemanticSegWordVO;
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
@FeignClient(
        value = "${kg.semantic.path:kg-services-semantic}",
        contextId = "semanticClient"
)
public interface SemanticClient {
    /**
     * 问答
     *
     * @param kgName
     * @param query
     * @return
     */
    @PostMapping({"qa/kbqa"})
    ApiReturn<QaAnswerDataRsp> query(@RequestParam("kgName") String kgName, @RequestBody QueryReq query);

    /**
     * 初始化意图
     *
     * @param kgName
     * @return
     */
    @PostMapping({"qa/graph/create"})
    ApiReturn create(@RequestParam("kgName") String kgName);

    /**
     * 实体识别
     *
     * @param var1
     * @return
     */
    @PostMapping({"qa/ner"})
    ApiReturn<List<SemanticSegWordVO>> ner(NerSearchReq var1);

    /**
     * 语义识别
     *
     * @param kgName
     * @param var2
     * @param var3
     * @return
     */
    @PostMapping({"qa/intent"})
    ApiReturn<IntentDataBean> intent(@ApiParam("图谱名称") @RequestParam("kgName") String kgName, @ApiParam("自然语言输入") @RequestParam("query") String var2, @RequestParam(value = "size", defaultValue = "5") int var3);

    /**
     * 推理
     *
     * @param kgName
     * @param var2
     * @return
     */
    @PostMapping({"reasoning/execute/{kgName}"})
    ApiReturn<GraphReasoningResultRsp> reasoning(@ApiParam("图谱名称") @PathVariable("kgName") String kgName, @RequestBody ReasoningReq var2);
}
