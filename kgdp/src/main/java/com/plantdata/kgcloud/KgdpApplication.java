package com.plantdata.kgcloud;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.plantdata.kgcloud.constant.CommonConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = CommonConstants.WebConst.ROOT_PKG)
@EnableApolloConfig
@EnableFeignClients(basePackages = CommonConstants.FeignConst.BASE_PKG)
@EnableTransactionManagement
public class KgdpApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(KgdpApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(KgdpApplication.class);
    }
}
