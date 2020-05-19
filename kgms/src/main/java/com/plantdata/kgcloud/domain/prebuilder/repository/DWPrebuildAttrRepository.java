package com.plantdata.kgcloud.domain.prebuilder.repository;

import com.plantdata.kgcloud.domain.prebuilder.entity.DWPrebuildAttr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DWPrebuildAttrRepository extends JpaRepository<DWPrebuildAttr, Integer> {


    @Modifying
    @Query(value = "delete from prebuild_attr where model_id = :id",nativeQuery = true)
    void deleteByModelId(@Param("id") Integer id);


    @Query(value = "select * from prebuild_attr where model_id = :modelId and `concept_id` in (:conceptIds)",nativeQuery = true)
    List<DWPrebuildAttr> findByModelAndConceptIds(@Param("modelId") Integer modelId, @Param("conceptIds") List<Integer> conceptIds);

    @Query(value = "select * from prebuild_attr where `concept_id` in (:conceptIds)",nativeQuery = true)
    List<DWPrebuildAttr> findByConceptIds(@Param("conceptIds") List<Integer> conceptIds);
}
