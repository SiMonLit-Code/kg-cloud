package com.plantdata.kgcloud.filter;

import com.google.common.collect.Lists;
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
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class ApkAuthFilter extends OncePerRequestFilter {
    @Autowired
    private SsoClient ssoClient;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    public static final String ADMIN_APK = "03c7a9376254ebb8a6b27706194";


    private static final List<String> ROBOT_ALLOW_PATHS = Lists.newArrayList("graphExplore/common/**",
            "infoBox/list/**", "graphExplore/path/**", "knowledgeRecommend/**", "graphExplore/relation/**", "graphExplore/timing/**"
    );

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
        boolean robotAllow = ROBOT_ALLOW_PATHS.stream().anyMatch(a -> antPathMatcher.match(a, requestUri));
        if (robotAllow) {
            isAllowed = true;
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
        //非管理员需要登录(兼容旧接口)
        if (!ADMIN_APK.equals(apk)) {
            Optional<LoginRsp> loginOpt = login(apk, httpServletResponse);
            if (!loginOpt.isPresent()) {
                return;
            }
            SessionHolder.setUserId(loginOpt.get().getToken());
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
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
