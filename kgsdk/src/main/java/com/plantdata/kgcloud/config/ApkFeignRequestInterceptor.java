package com.plantdata.kgcloud.config;

import com.plantdata.kgcloud.util.WebUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApkFeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(WebUtils.AUTHORIZATION, WebUtils.getAuthorization());
    }
}
