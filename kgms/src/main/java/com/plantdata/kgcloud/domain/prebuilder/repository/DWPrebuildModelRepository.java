package com.plantdata.kgcloud.domain.prebuilder.repository;

import com.plantdata.kgcloud.domain.prebuilder.entity.DWPrebuildModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DWPrebuildModelRepository extends JpaRepository<DWPrebuildModel, Integer> {

    Page<DWPrebuildModel> findAll(Specification<DWPrebuildModel> spec, Pageable pageable);

    @Query(value = "select * from prebuild_model where user_id = :userId and database_id = :databaseId and status = '1'",nativeQuery = true)
    DWPrebuildModel findByUserIdAndDatabaseId(@Param("userId") String userId, @Param("databaseId") Long databaseId);

    @Query(value = "select model_type from prebuild_model where `status` = '1' and (permission = 1 or user_id = :userId) group by model_type",nativeQuery = true)
    List<String> getModelTypes(@Param("userId") String userId);

    @Query(value = "select model_type from prebuild_model group by model_type",nativeQuery = true)
    List<String> getAdminModelTypes();

    @Query(value = "select model_type from prebuild_model where user_id = :userId group by model_type",nativeQuery = true)
    List<String> getUserManageModelTypes(@Param("userId")String userId);

}
