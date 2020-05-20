package com.plantdata.kgcloud.sdk;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author cjw
 * @date 2020/5/20  10:03
 */
@FeignClient(value = "kgms", path = "repo", contextId = "RepositoryClient")
public interface RepositoryClient {
}
