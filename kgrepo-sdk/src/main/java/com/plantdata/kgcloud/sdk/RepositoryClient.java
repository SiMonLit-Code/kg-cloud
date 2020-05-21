package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.rsp.RepositoryLogMenuRsp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author cjw
 * @date 2020/5/20  10:03
 */
@FeignClient(value = "kgms", path = "repository", contextId = "RepositoryClient")
public interface RepositoryClient {


    @GetMapping("log/menu")
    ApiReturn<List<RepositoryLogMenuRsp>> logMenuRsp();
}
