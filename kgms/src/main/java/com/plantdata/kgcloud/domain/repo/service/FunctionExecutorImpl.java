package com.plantdata.kgcloud.domain.repo.service;

import com.plantdata.kgcloud.domain.repo.checker.ServiceChecker;
import com.plantdata.kgcloud.domain.repo.factory.HandlerStreamFactory;
import com.plantdata.kgcloud.domain.repo.factory.RepositoryRootFactory;
import com.plantdata.kgcloud.domain.repo.factory.ServiceCheckerFactory;
import com.plantdata.kgcloud.domain.repo.model.req.DealDTO;
import com.plantdata.kgcloud.domain.repo.stream.HandlerStream;
import com.plantdata.kgcloud.domain.repo.model.RepositoryRoot;
import org.springframework.stereotype.Service;

/**
 * @author cjw
 * @date 2020/5/15  12:11
 */
@Service
public class FunctionExecutorImpl implements FunctionExecutor {

    public RepositoryRoot buildRepositoryManager(RepositoryRootFactory rootFactory) {
        return rootFactory.factory();
    }


    private void check(RepositoryRoot manager) {
        ServiceChecker checker = new ServiceCheckerFactory(manager).factory();
        checker.check();
    }

    private HandlerStream handlerStream(RepositoryRoot manager) {
        return new HandlerStreamFactory(manager).factory();
    }

    @Override
    public <T> T execute(RepositoryRootFactory rootFactory, Class<T> clazz) {
        RepositoryRoot repositoryRoot = buildRepositoryManager(rootFactory);
        check(repositoryRoot);
        HandlerStream handlerStream = handlerStream(repositoryRoot);
        DealDTO handler = handlerStream.handler();
        assert handler.rsp().getClass().isInstance(clazz) ;
        return (T) handler;
    }
}
