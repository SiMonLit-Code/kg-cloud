package com.plantdata.kgcloud.domain.repo.service;

import com.plantdata.kgcloud.domain.repo.factory.RepositoryRootFactory;
import com.plantdata.kgcloud.domain.repo.model.req.DealDTO;

/**
 * @author cjw
 * @date 2020/5/15  13:20
 */
public interface FunctionExecutor {

    <T> T  execute(RepositoryRootFactory rootFactory,Class<T> clazz);
}
