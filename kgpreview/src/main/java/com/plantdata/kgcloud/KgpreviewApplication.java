package com.plantdata.kgcloud;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.plantdata.kgcloud.constant.CommonConstants;
import com.plantdata.kgcloud.util.PropertyUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = CommonConstants.WebConst.ROOT_PKG)
@EnableApolloConfig
@EnableFeignClients(basePackages = CommonConstants.FeignConst.BASE_PKG)
public class KgpreviewApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(KgpreviewApplication.class, args);
        PropertyUtils.printEnvAll(configurableApplicationContext);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(KgpreviewApplication.class);
    }
}
