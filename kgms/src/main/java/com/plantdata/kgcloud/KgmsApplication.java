package com.plantdata.kgcloud;

import ai.plantdata.cloud.constant.CommonConstants;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableDiscoveryClient
@SpringBootApplication
@EnableApolloConfig
@EnableTransactionManagement
@EnableFeignClients(basePackages = {"ai.plantdata","com.plantdata"})
@EnableJpaAuditing
public class KgmsApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(KgmsApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(KgmsApplication.class);
    }

}
