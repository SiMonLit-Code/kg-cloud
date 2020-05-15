package com.plantdata.kgcloud.domain.repo.model;

import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

/**
 * @author cjw
 * @date 2020/5/15  12:06
 */
public interface ConsulService {

    List<RepositoryHandler> handlers();

     DiscoveryClient discoveryClient();
}
