package com.plantdata.kgcloud.domain.repo.service;

import com.hiekn.data.util.JsonUtils;
import com.plantdata.kgcloud.domain.repo.checker.ServiceChecker;
import com.plantdata.kgcloud.domain.repo.factory.HandlerStreamFactory;
import com.plantdata.kgcloud.domain.repo.factory.RepositoryRootFactory;
import com.plantdata.kgcloud.domain.repo.factory.ServiceCheckerFactory;
import com.plantdata.kgcloud.domain.repo.stream.HandlerStream;
import com.plantdata.kgcloud.domain.repo.model.RepositoryRoot;
import org.springframework.stereotype.Service;

/**
 * @author cjw
 * @date 2020/5/15  12:11
 */
@Service
public class FunctionExecutorImpl  implements FunctionExecutor  {

    public RepositoryRoot buildRepositoryManager(RepositoryRootFactory rootFactory) {
        return rootFactory.factory();
    }


    private void check(RepositoryRoot manager) {
        ServiceChecker checker = new ServiceCheckerFactory(manager).factory();
        checker.check();
    }

    private  HandlerStream handlerStream(RepositoryRoot manager) {
        return new HandlerStreamFactory(manager).factory();
    }

    @Override
    public <R> R execute(RepositoryRootFactory rootFactory,Class<R> rspType) {
        RepositoryRoot repositoryRoot = buildRepositoryManager(rootFactory);
        check(repositoryRoot);
        HandlerStream handlerStream = handlerStream(repositoryRoot);
        Object handler = handlerStream.handler();
        return JsonUtils.fromJson(JsonUtils.toJson(handler),rspType);
    }
}
