package com.plantdata.kgcloud.domain.repo.repository;

import com.plantdata.kgcloud.domain.repo.enums.RepositoryLogEnum;
import com.plantdata.kgcloud.domain.repo.model.RepositoryUseLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author cjw
 * @date 2020/5/18  15:06
 */
public interface RepositoryUseLogRepository extends JpaRepository<RepositoryUseLog, Integer> {

    List<RepositoryUseLog> deleteByBusinessIdInAndLogType(List<Integer> businessId, RepositoryLogEnum type);


    List<RepositoryUseLog> findAllByUserIdAndRepositoryIdIn(String userId, Collection<Integer> repositoryIds);

    List<RepositoryUseLog> findAllByUserIdAndLogType(String userId, RepositoryLogEnum type);
}
