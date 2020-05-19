package com.plantdata.kgcloud.domain.repo.repository;

import com.plantdata.kgcloud.domain.repo.model.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author cjw
 * @date 2020/5/18  16:11
 */
public interface RepositoryRepository extends JpaRepository<Repository, Integer> {
}
