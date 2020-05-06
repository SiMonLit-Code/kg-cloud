package com.plantdata.kgcloud.config;

import com.plantdata.kgcloud.util.WebUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class SsoSecurityConfig extends ResourceServerConfigurerAdapter {

    @Value("${sso.allowedPaths:}")
    private String[] ignorePaths;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors();
        http.authorizeRequests()
                .antMatchers(WebUtils.DEFAULT_IGNORE)
                .permitAll()
                .anyRequest()
                .authenticated();
        if (ignorePaths != null && ignorePaths.length > 0) {
            http.authorizeRequests().antMatchers(ignorePaths).permitAll();
        }

    }

}
