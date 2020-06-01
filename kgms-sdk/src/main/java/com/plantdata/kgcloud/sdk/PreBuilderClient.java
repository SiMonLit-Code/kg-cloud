package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.req.PreBuilderGraphMapReq;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author czj
 * @version 1.0
 * @date 2020/06/01 14:03
 */
@FeignClient(value = "kgms", path = "builder", contextId = "builder")
public interface PreBuilderClient {

    @PostMapping("/save/graph/map")
    ApiReturn<JSONObject> saveGraphMap(@RequestBody PreBuilderGraphMapReq preBuilderGraphMapReq);
}
