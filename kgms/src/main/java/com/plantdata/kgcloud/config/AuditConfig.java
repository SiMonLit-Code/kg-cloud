package com.plantdata.kgcloud.config;

import com.plantdata.kgcloud.security.SessionHolder;
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
        return Optional.of(SessionHolder.getUserId());
    }
}
