package com.plantdata.kgcloud.domain.repo.factory;

import com.plantdata.kgcloud.domain.repo.stream.ConsulServiceHandlerStream;
import com.plantdata.kgcloud.domain.repo.stream.HandlerStream;
import com.plantdata.kgcloud.domain.repo.model.RepositoryRoot;
import com.plantdata.kgcloud.exception.BizException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author cjw
 * @date 2020/5/15  12:27
 */
@Slf4j
public class HandlerStreamFactory {
    private final RepositoryRoot manager;

    public HandlerStreamFactory(RepositoryRoot manager) {
        this.manager = manager;
    }

    public HandlerStream factory() {
        switch (manager.checkType()) {
            case CONSUL:
                return new ConsulServiceHandlerStream(manager);
            case MONGO:
                log.warn("todo mongo");
                return null;
            case FILE:
                log.warn("todo file");
                return null;
            default:
                throw new BizException("代码有误");
        }

    }


}
