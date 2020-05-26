package com.plantdata.kgcloud.domain.repo.repository;

import com.plantdata.kgcloud.domain.repo.entity.RepoBaseMenu;
import com.plantdata.kgcloud.domain.repo.entity.RepoMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepoBaseMenuRepository extends JpaRepository<RepoBaseMenu, Integer> {

}
