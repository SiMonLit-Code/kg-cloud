package com.plantdata.kgcloud.filter;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.config.CurrentUser;
import com.plantdata.kgcloud.constant.CommonErrorCode;
import com.plantdata.kgcloud.sdk.SsoClient;
import com.plantdata.kgcloud.sdk.rsp.LoginRsp;
import com.plantdata.kgcloud.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Component
public class ApkAuthFilter extends OncePerRequestFilter {
    private static final String ADMIN_APK = "03c7a9376254ebb8a6b27706194";
    @Autowired
    private SsoClient ssoClient;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if(authentication instanceof OAuth2Authentication) {
                OAuth2Authentication oauth = (OAuth2Authentication) authentication;
                OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) oauth.getDetails();
                CurrentUser.setAdmin(false);
                CurrentUser.setToken(details.getTokenType() + " " + details.getTokenValue());
                filterChain.doFilter(request, response);
                return;
            }
        }

        String apk = WebUtils.getKgApk(request);
        if (!StringUtils.hasText(apk)) {
            filterChain.doFilter(request, response);
            return;
        }
        if(StringUtils.hasText(CurrentUser.getToken())){
            filterChain.doFilter(request, response);
            return;
        }
        String authorization = WebUtils.getAuthorization(request);
        if(StringUtils.hasText(authorization)){
            filterChain.doFilter(request, response);
            return;
        }
        //非管理员需要登录(兼容旧接口)
        Optional<LoginRsp> loginOpt = login(apk, response);
        if (!loginOpt.isPresent()) {
            filterChain.doFilter(request, response);
            return;
        }
        LoginRsp loginRsp = loginOpt.get();
        CurrentUser.setAdmin(loginRsp.isAdmin());
        CurrentUser.setToken(loginRsp.getToken());


        UsernamePasswordAuthenticationToken result =
                new UsernamePasswordAuthenticationToken("a","b", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        eventPublisher.publishEvent(new AuthenticationSuccessEvent(result));
        SecurityContextHolder.getContext().setAuthentication(result);
        filterChain.doFilter(request, response);
    }

    private Optional<LoginRsp> login(String apk, HttpServletResponse httpServletResponse) throws IOException {
        ApiReturn<LoginRsp> loginRspApiReturn;
        try {
            loginRspApiReturn = this.ssoClient.loginByApk(apk);
        } catch (Exception e) {
            WebUtils.sendResponse(httpServletResponse, ApiReturn.fail(CommonErrorCode.INTERNAL_SERVER_ERROR));
            return Optional.empty();
        }
        if (loginRspApiReturn == null) {
            WebUtils.sendResponse(httpServletResponse, ApiReturn.fail(CommonErrorCode.INTERNAL_SERVER_ERROR));
            return Optional.empty();
        }
        if (CommonErrorCode.SUCCESS.getErrorCode() != loginRspApiReturn.getErrCode()) {
            WebUtils.sendResponse(httpServletResponse, loginRspApiReturn);
            return Optional.empty();
        }
        return Optional.of(loginRspApiReturn.getData());
    }
}
