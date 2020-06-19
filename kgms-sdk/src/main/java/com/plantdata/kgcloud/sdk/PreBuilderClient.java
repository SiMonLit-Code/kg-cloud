package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.exection.client.PreBuilderClientEx;
import com.plantdata.kgcloud.sdk.req.ReasoningExecuteReq;
import com.plantdata.kgcloud.sdk.req.ReasoningQueryReq;
import com.plantdata.kgcloud.sdk.req.StandardSearchReq;
import com.plantdata.kgcloud.sdk.req.app.sematic.ReasoningReq;
import com.plantdata.kgcloud.sdk.rsp.ReasoningRsp;
import com.plantdata.kgcloud.sdk.rsp.StandardTemplateRsp;
import com.plantdata.kgcloud.sdk.rsp.app.RelationReasonRuleRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.GraphReasoningResultRsp;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author czj
 * @version 1.0
 * @date 2020/6/8 10:59
 */
@FeignClient(value = "kgms", path = "builder/", contextId = "preBuilderClient",fallback = PreBuilderClientEx.class)
public interface PreBuilderClient {


    /**
     * 行业标准读取
     *
     * @param req
     * @return
     */
    @PostMapping("/standard/list")
    ApiReturn standardList(@RequestBody StandardSearchReq req) ;


    /**
     * 模式获取
     *
     * @param ids
     * @return
     */
    @PostMapping("/find/ids")
    ApiReturn<List<StandardTemplateRsp>> findIds(@RequestBody List<Integer> ids);
}
