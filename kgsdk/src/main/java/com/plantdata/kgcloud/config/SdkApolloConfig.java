package com.plantdata.kgcloud.config;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@EnableApolloConfig
@Configuration
public class SdkApolloConfig {

    @Value("${presto.dw.alias}")
    private String prestoDwAlias;
}
