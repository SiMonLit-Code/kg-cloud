package ai.plantdata.kgcloud.filter;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.constant.CommonErrorCode;
import ai.plantdata.cloud.web.util.WebUtils;
import ai.plantdata.kgcloud.config.CurrentUser;
import ai.plantdata.kgcloud.sdk.SsoClient;
import ai.plantdata.kgcloud.sdk.rsp.LoginRsp;
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
import java.util.LinkedHashMap;
import java.util.List;
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
            if (authentication instanceof OAuth2Authentication) {
                OAuth2Authentication oauth = (OAuth2Authentication) authentication;
                OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) oauth.getDetails();
                Object principal = oauth.getPrincipal();
                boolean isAdmin = false;
                if (principal instanceof LinkedHashMap) {
                    Object authorities = ((LinkedHashMap) principal).get("authorities");
                    if (authorities instanceof List) {
                        isAdmin = ((List) authorities).stream().anyMatch(a -> {
                            if (a instanceof LinkedHashMap) {
                                LinkedHashMap<String, String> authMap = (LinkedHashMap<String, String>) a;
                                return authMap.containsKey("authority") && "ROLE_ADMIN".equals(authMap.get("authority"));
                            }
                            return false;
                        });
                    }
                }
                CurrentUser.setAdmin(isAdmin);
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
                new UsernamePasswordAuthenticationToken("a", "b", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        eventPublisher.publishEvent(new AuthenticationSuccessEvent(result));
        SecurityContextHolder.getContext().setAuthentication(result);
        filterChain.doFilter(request, response);
    }

    private Optional<LoginRsp> login(String apk, HttpServletResponse httpServletResponse) throws IOException {
        ApiReturn<LoginRsp> loginRspApiReturn;
        try {
            loginRspApiReturn = this.ssoClient.loginByApk(apk);
        } catch (Exception e) {
            log.error(" ", e);
            WebUtils.sendResponse(httpServletResponse, ApiReturn.fail(CommonErrorCode.INTERNAL_SERVER_ERROR));
            return Optional.empty();
        }
        if (loginRspApiReturn == null) {
            WebUtils.sendResponse(httpServletResponse, ApiReturn.fail(CommonErrorCode.INTERNAL_SERVER_ERROR));
            return Optional.empty();
        }
        if (CommonErrorCode.SUCCESS.getErrorCode() != loginRspApiReturn.getErrCode()) {
            log.error(" ", loginRspApiReturn);
            WebUtils.sendResponse(httpServletResponse, loginRspApiReturn);
            return Optional.empty();
        }
        return Optional.of(loginRspApiReturn.getData());
    }
}
