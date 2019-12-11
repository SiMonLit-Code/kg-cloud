package com.plantdata.kgcloud.filter;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.CommonErrorCode;
import com.plantdata.kgcloud.sdk.SsoClient;
import com.plantdata.kgcloud.sdk.rsp.LoginRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.security.SsoAuthFilter;
import com.plantdata.kgcloud.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class ApkAuthFilter extends OncePerRequestFilter {
    @Autowired
    private SsoClient ssoClient;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = httpServletRequest.getRequestURI();
        boolean isAllowed = false;
        for (String allowedPath : SsoAuthFilter.DEFAULT_ALLOWED_PATHS) {
            if (antPathMatcher.match(allowedPath, requestUri)) {
                isAllowed = true;
                break;
            }
        }
        if (isAllowed) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        String apk = WebUtils.getKgApk(httpServletRequest);
        if (StringUtils.isEmpty(apk)) {
            log.debug("ApkAuthFilter reject request uri [{}]", requestUri);
            WebUtils.sendResponse(httpServletResponse, ApiReturn.fail(CommonErrorCode.BAD_REQUEST));
            return;
        }
        ApiReturn<LoginRsp> loginRspApiReturn = null;
        try {
            loginRspApiReturn = this.ssoClient.loginByApk(apk);
        } catch (Exception e) {
            WebUtils.sendResponse(httpServletResponse, ApiReturn.fail(CommonErrorCode.INTERNAL_SERVER_ERROR));
            return;
        }
        if (loginRspApiReturn == null) {
            WebUtils.sendResponse(httpServletResponse, ApiReturn.fail(CommonErrorCode.INTERNAL_SERVER_ERROR));
            return;
        }
        if (CommonErrorCode.SUCCESS.getErrorCode() != loginRspApiReturn.getErrCode()) {
            WebUtils.sendResponse(httpServletResponse, loginRspApiReturn);
            return;
        }
        SessionHolder.setUserId(loginRspApiReturn.getData().getToken());
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
