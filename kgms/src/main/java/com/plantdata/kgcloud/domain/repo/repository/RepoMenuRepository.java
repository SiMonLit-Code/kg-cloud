package com.plantdata.kgcloud.domain.repo.repository;

import com.plantdata.kgcloud.domain.repo.entity.RepoMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author cjw
 * @date 2020/5/21  19:02
 */
public interface RepoMenuRepository extends JpaRepository<RepoMenu, Integer> {


    List<RepoMenu> findAllByRepositoryIdIn(List<Integer> repositoryIds);

    List<RepoMenu> findAllByMenuIdIn(List<Integer> menuIds);

    void deleteAllByRepositoryId(int repositoryId);
}
