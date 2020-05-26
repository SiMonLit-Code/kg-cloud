package com.plantdata.kgcloud.domain.repo.repository;

import com.plantdata.kgcloud.domain.repo.entity.RepoItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author cjw
 * @date 2020/5/18  16:11
 */
public interface RepoItemRepository extends JpaRepository<RepoItem, Integer> {

}
