package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/13 16:24
 */
@FeignClient(value = "kgms", path = "wrapper", contextId = "wrapperClient")
public interface WrapperClient {

    /**
     * 预览
     *
     * @param html
     * @param config
     * @return
     */
    @PostMapping("preview")
    ApiReturn<Map<String, Object>> parse(@ApiParam(required = true) @RequestParam String html,
                                         @ApiParam(value = "字段配置", required = true) @RequestParam String config);

    /**
     * 列表
     *
     * @return
     */
    @GetMapping("formatter/list")
    ApiReturn<List<Map<String, String>>> formatterList();
}
