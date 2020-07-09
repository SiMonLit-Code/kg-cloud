package com.plantdata.kgcloud;

import ai.plantdata.cloud.constant.CommonConstants;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * @author wanglong
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableApolloConfig
@EnableFeignClients(basePackages = {"ai.plantdata","com.plantdata"})
public class KgsdkApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(KgsdkApplication.class, args);
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(KgsdkApplication.class);
    }


}
