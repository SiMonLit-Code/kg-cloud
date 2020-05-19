package com.plantdata.kgcloud.domain.repo.checker.service;

import com.plantdata.kgcloud.domain.repo.model.RepositoryHandler;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

/**
 * @author cjw
 * @date 2020/5/15  12:06
 */
public interface ConsulService extends Service {

    List<RepositoryHandler> handlers();

     DiscoveryClient discoveryClient();
}
