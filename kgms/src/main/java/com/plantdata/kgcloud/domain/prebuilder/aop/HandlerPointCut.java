package com.plantdata.kgcloud.domain.prebuilder.aop;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.repo.entity.RepoHandler;
import com.plantdata.kgcloud.domain.repo.enums.HandleType;
import com.plantdata.kgcloud.domain.repo.repository.RepoHandlerRepository;
import com.plantdata.kgcloud.domain.repo.service.RepositoryService;
import com.plantdata.kgcloud.util.JacksonUtils;
import com.plantdata.kgcloud.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bovin
 * @description
 * @since 2020-05-27 22:39
 **/
@Component
@Aspect
@Slf4j
public class HandlerPointCut {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RepoHandlerRepository repoHandlerRepository;


    @Autowired
    private RepositoryService repositoryService;

//    @Pointcut("@annotation(preHandler)")
//    public void preHandlerPointCut(PreHandler preHandler) {
//    }

    @Pointcut("@annotation(postHandler)")
    public void postHandlerPointCut(PostHandler postHandler) {
    }

    @Around(value = "postHandlerPointCut(postHandler)", argNames = "p,postHandler")
    public Object post(ProceedingJoinPoint p, PostHandler postHandler) throws Throwable {
        Object[] args = p.getArgs();
        Object o = p.proceed();
        int id = postHandler.id();
        if (id > 0) {
            List<RepoHandler> repoHandlers = repoHandlerRepository.findByInterfaceIdAndHandleType(id, HandleType.AFTER.toString());
            repoHandlers.sort(Comparator.comparing(RepoHandler::getRank));
            for (RepoHandler repoHandler : repoHandlers) {
                if (repositoryService.state(repoHandler.getRepoId())) {
                    try {
                        o = handle(args, o, repoHandler);
                    } catch (RuntimeException e) {
                        log.error("后置处理器调用失败...", e);
                    }
                }
            }
            return o;
        } else {
            return o;
        }
    }

    private URI getUri(RepoHandler repoHandler) {
        ServiceInstance choose = loadBalancerClient.choose(repoHandler.getRequestServerName());
        return UriComponentsBuilder.fromPath(repoHandler.getRequestUrl())
                .scheme("http")
                .port(choose.getPort())
                .host(choose.getHost())
                .build()
                .toUri();
    }

    private Object handle(Object req, Object rsp, RepoHandler repoHandler) {
        String method = repoHandler.getRequestMethod().toUpperCase();
        HttpMethod requestMethod = HttpMethod.resolve(method);
        HttpHeaders httpHeaders = new HttpHeaders();
        String authorization = WebUtils.getAuthorization();
        if (authorization != null) {
            httpHeaders.add(WebUtils.AUTHORIZATION, authorization);
        }
        HashMap<String, Object> body = new HashMap<>();
        body.put("request", req);
        body.put("response", rsp);
        HttpEntity<Map> httpEntity = new HttpEntity<>(body, httpHeaders);
        URI uri = getUri(repoHandler);
        log.info("调用服务 ：{}, 调用接口 ：{} {}", repoHandler.getRequestServerName(), method, uri.toString());
        if (log.isDebugEnabled()) {
            log.debug("请求参数：", JacksonUtils.writeValueAsString(body));
        }
        try {
            ResponseEntity<ApiReturn> exchange = restTemplate.exchange(uri, requestMethod, httpEntity, ApiReturn.class);
            return exchange.getBody();
        } catch (Exception e) {
            throw new RuntimeException("调用服务：" + repoHandler.getRequestServerName() + ",调用接口: " + uri.toString() + " 失败");
        }
    }
}
