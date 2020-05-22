package com.plantdata.kgcloud.domain.repo.repository;

import com.plantdata.kgcloud.domain.repo.model.RepositoryMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author cjw
 * @date 2020/5/21  19:02
 */
public interface RepositoryMenuRepository extends JpaRepository<RepositoryMenu,Integer> {


    List<RepositoryMenu> findAllByRepositoryIdIn(List<Integer> repositoryIds);

    List<RepositoryMenu> findAllByMenuIdIn(List<Integer> menuIds);

    void deleteAllByRepositoryId(int repositoryId);
}
