package com.plantdata.kgcloud.domain.repo.service;

import com.plantdata.kgcloud.domain.repo.factory.RepositoryRootFactory;

/**
 * @author cjw
 * @date 2020/5/15  13:20
 */
public interface FunctionExecutor {

    <R> R execute(RepositoryRootFactory rootFactory, Class<R> rspType);
}
