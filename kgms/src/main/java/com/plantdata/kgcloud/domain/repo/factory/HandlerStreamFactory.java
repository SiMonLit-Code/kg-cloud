package com.plantdata.kgcloud.domain.repo.factory;

import com.plantdata.kgcloud.domain.repo.model.HandlerStream;
import com.plantdata.kgcloud.domain.repo.model.RepositoryRoot;

/**
 * @author cjw
 * @date 2020/5/15  12:27
 */
public class HandlerStreamFactory {
    private final RepositoryRoot manager;

    public HandlerStreamFactory(RepositoryRoot manager) {
        this.manager = manager;
    }

    public <R> HandlerStream<R> factory() {
        return null;
    }
}
