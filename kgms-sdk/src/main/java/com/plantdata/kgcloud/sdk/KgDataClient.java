package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.req.app.EntityQueryReq;
import com.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.data.RelationUpdateReq;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 14:02
 */
@FeignClient(value = "kgms", path = "kgData", contextId = "kgDataClient")
public interface KgDataClient {

    /**
     * 第三方模型抽取
     *
     * @param modelId    模型id
     * @param input      。
     * @param configList 配置
     * @return 。
     */
    @PostMapping("extract/thirdModel/{modelId}")
    ApiReturn<Object> extractThirdModel(@PathVariable("modelId") Long modelId,
                                        @RequestParam("input") String input, @RequestBody List<Map<String, String>> configList);
}
