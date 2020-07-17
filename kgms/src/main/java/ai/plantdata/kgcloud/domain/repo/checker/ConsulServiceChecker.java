package ai.plantdata.kgcloud.domain.repo.checker;

import ai.plantdata.kgcloud.domain.repo.model.RepoCheckConfig;
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
    private final List<RepoCheckConfig> checkConfigs;

    public ConsulServiceChecker(DiscoveryClient discoveryClient, List<RepoCheckConfig> checkConfigs) {
        this.discoveryClient = discoveryClient;
        this.checkConfigs = checkConfigs;
    }

    @Override
    public boolean check() {
        return checkConfigs.stream()
                .anyMatch(a -> {
                    List<ServiceInstance> instances = discoveryClient.getInstances(a.getContent());
                    boolean res = instances == null || instances.size() == 0;
                    if (res) {
                        log.error("serverName:{}", a.getContent());
                        //throw new BizException("插件实例未找到");
                        return false;
                    }
                    return true;
                });
    }
}
