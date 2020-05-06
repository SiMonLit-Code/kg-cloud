package com.plantdata.kgcloud.config;

import com.plantdata.kgcloud.util.WebUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class ApkFeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (StringUtils.hasText(CurrentUser.getToken())) {
            requestTemplate.header(WebUtils.AUTHORIZATION, CurrentUser.getToken());
        }
    }
}
