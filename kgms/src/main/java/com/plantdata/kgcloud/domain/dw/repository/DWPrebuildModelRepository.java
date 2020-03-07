package com.plantdata.kgcloud.domain.dw.repository;

import com.plantdata.kgcloud.domain.dw.entity.DWPrebuildModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface DWPrebuildModelRepository extends JpaRepository<DWPrebuildModel, Integer> {


    Page<DWPrebuildModel> findAll(Specification<DWPrebuildModel> spec, Pageable pageable);

    @Query(value = "select * from dw_prebuild_model where user_id = :userId and database_id = :databaseId",nativeQuery = true)
    DWPrebuildModel findByUserIdAndDatabaseId(@Param("userId") String userId, @Param("databaseId") Long databaseId);
}
