package com.plantdata.kgcloud.domain.repo.repository;

import com.plantdata.kgcloud.domain.repo.entity.RepoHandler;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Bovin
 * @description
 * @since 2020-05-27 22:59
 **/
public interface RepoHandlerRepository extends JpaRepository<RepoHandler, Integer> {
    List<RepoHandler> findByInterfaceIdAndHandleType(int interfaceId, String handleType);
}
