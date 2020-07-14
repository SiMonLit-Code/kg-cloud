package ai.plantdata.kgcloud.config;

import ai.plantdata.cloud.web.util.SessionHolder;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-05 13:03
 **/
@Component
public class AuditConfig implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(SessionHolder.getUserId());
    }
}
