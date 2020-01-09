package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2020/1/9 15:13
 */
@FeignClient(value = "kgms", path = "/merge", contextId = "mergeClient")
public interface MergeClient {

    /**
     * 手工创建融合实体
     *
     * @param kgName kgName
     * @param ids    实体id
     * @return
     */
    @PostMapping("wait/entity/create/{kgName}")
    ApiReturn<String> createMergeEntity(@PathVariable("kgName") String kgName, @RequestBody List<Long> ids);
}
