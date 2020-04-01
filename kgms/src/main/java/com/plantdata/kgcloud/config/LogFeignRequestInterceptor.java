package com.plantdata.kgcloud.config;

import com.plantdata.kgcloud.domain.edit.util.ThreadLocalUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class LogFeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String batchNo = ThreadLocalUtils.getBatchNo();
        if (StringUtils.hasText(batchNo)) {
            requestTemplate.header("actionId", batchNo);
        }
    }
}
