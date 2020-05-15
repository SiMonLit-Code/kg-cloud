package com.plantdata.kgcloud.domain.repo.checker;

import com.plantdata.kgcloud.domain.repo.model.ConsulService;
import com.plantdata.kgcloud.domain.repo.model.RepositoryHandler;
import com.plantdata.kgcloud.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

/**
 * @author cjw
 * @date 2020/5/15  11:11
 */
@Slf4j
public class ConsulServiceChecker implements ServiceChecker {

    public final DiscoveryClient discoveryClient;
    private List<RepositoryHandler> handlers;

    public ConsulServiceChecker(DiscoveryClient discoveryClient, List<RepositoryHandler> handlers) {
        this.discoveryClient = discoveryClient;
        this.handlers = handlers;
    }

    @Override
    public void check() {
        handlers.forEach(a -> {
            List<ServiceInstance> instances = discoveryClient.getInstances(a.getRequestServerName());
            if (instances == null || instances.size() == 0) {
                log.error("serverName:{}", a.getRequestServerName());
                throw new BizException("插件实例未找到");
            }
        });
    }
}
