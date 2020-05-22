package com.plantdata.kgcloud.domain.repo.service;

import com.plantdata.kgcloud.domain.repo.enums.RepositoryLogEnum;

import java.util.Set;

/**
 * @author cjw
 * @date 2020/5/22  13:26
 */
public interface RepositoryUseLogService {

    void save(RepositoryLogEnum logEnum, int id, String userId);

    Set<Integer> listRepositoryId(String userId);

    Set<Integer> listMenuId(String userId);

    void deleteByRepositoryId(Integer repositoryId);

}
