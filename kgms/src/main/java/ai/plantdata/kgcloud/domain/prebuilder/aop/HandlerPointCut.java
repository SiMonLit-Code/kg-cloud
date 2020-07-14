package ai.plantdata.kgcloud.domain.prebuilder.aop;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.cloud.web.util.SessionHolder;
import ai.plantdata.cloud.web.util.WebUtils;
import ai.plantdata.kgcloud.domain.repo.entity.RepoHandler;
import ai.plantdata.kgcloud.domain.repo.enums.HandleType;
import ai.plantdata.kgcloud.domain.repo.service.RepositoryService;
import ai.plantdata.kgcloud.domain.repo.repository.RepoHandlerRepository;
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
import java.util.List;

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
        HandlerReq handlerResponse = response(args);
        Object o = p.proceed();
        int id = postHandler.id();
        if (id > 0) {
            List<RepoHandler> repoHandlers = repoHandlerRepository.findByInterfaceIdAndHandleType(id, HandleType.AFTER.toString());
            repoHandlers.sort(Comparator.comparing(RepoHandler::getRank));
            for (RepoHandler repoHandler : repoHandlers) {
                if (repositoryService.state(repoHandler.getRepoId())) {
                    try {
                        o = handle(handlerResponse, o, repoHandler);
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

    private HandlerReq response(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof HandlerReq) {
                return (HandlerReq) arg;
            }
        }
        return new DefaultHandlerReq();
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

    private Object handle(HandlerReq req, Object rsp, RepoHandler repoHandler) {
        String method = repoHandler.getRequestMethod().toUpperCase();
        HttpMethod requestMethod = HttpMethod.resolve(method);
        HttpHeaders httpHeaders = new HttpHeaders();
        String authorization = SessionHolder.getAuthorization();
        if (authorization != null) {
            httpHeaders.add(WebUtils.AUTHORIZATION, authorization);
        }
        req.setResponse(rsp);
        HttpEntity<HandlerReq> httpEntity = new HttpEntity<>(req, httpHeaders);
        URI uri = getUri(repoHandler);
        log.info("调用服务 ：{}, 调用接口 ：{} {}", repoHandler.getRequestServerName(), method, uri.toString());
        if (log.isDebugEnabled()) {
            log.debug("请求参数：", JacksonUtils.writeValueAsString(req));
        }
        try {
            ResponseEntity<ApiReturn> exchange = restTemplate.exchange(uri, requestMethod, httpEntity, ApiReturn.class);
            return exchange.getBody();
        } catch (Exception e) {
            throw new RuntimeException("调用服务：" + repoHandler.getRequestServerName() + ",调用接口: " + uri.toString() + " 失败");
        }
    }
}
