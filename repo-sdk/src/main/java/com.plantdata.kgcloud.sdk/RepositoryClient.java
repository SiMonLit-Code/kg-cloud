package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.sdk.rsp.RepositoryLogMenuRsp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author cjw
 * @date 2020/5/20  10:03
 */
@FeignClient(value = "kgms", path = "repo", contextId = "RepositoryClient")
public interface RepositoryClient {

    @PostMapping("menu")
    List<RepositoryLogMenuRsp> logMenuRsp(@RequestBody List<Integer> menuIds);
}
