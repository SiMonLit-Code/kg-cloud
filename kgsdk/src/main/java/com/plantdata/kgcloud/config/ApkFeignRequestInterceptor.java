package com.plantdata.kgcloud.config;

import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.WebUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class ApkFeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = SessionHolder.getUserId();
        if (!StringUtils.isEmpty(token)) {
            requestTemplate.header(WebUtils.KG_AUTHORIZATION, token);
        }
        if (CurrentUser.isAdmin()) {
            requestTemplate.header(WebUtils.ADMIN, "true");
        }
    }
}
