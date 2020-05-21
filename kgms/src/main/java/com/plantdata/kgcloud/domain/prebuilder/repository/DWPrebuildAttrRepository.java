package com.plantdata.kgcloud.domain.prebuilder.repository;

import com.plantdata.kgcloud.domain.prebuilder.entity.DWPrebuildAttr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DWPrebuildAttrRepository extends JpaRepository<DWPrebuildAttr, Integer> {

    @Query(value = "select * from prebuild_attr where `concept_id` in (:conceptIds)",nativeQuery = true)
    List<DWPrebuildAttr> findByConceptIds(@Param("conceptIds") List<Integer> conceptIds);
}
