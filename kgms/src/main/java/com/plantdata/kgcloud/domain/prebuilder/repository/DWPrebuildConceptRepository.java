package com.plantdata.kgcloud.domain.prebuilder.repository;

import com.plantdata.kgcloud.domain.prebuilder.entity.DWPrebuildConcept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DWPrebuildConceptRepository extends JpaRepository<DWPrebuildConcept, Integer> {

    @Modifying
    @Query(value = "delete from dw_prebuild_concept where model_id = :id",nativeQuery = true)
    void deleteByModelId(Integer id);



    @Query(value = "select * from dw_prebuild_concept where model_id = :modelId and `id` in (:conceptIds)",nativeQuery = true)
    List<DWPrebuildConcept> findByModelAndConceptIds(@Param("modelId") Integer modelId, @Param("conceptIds") List<Integer> conceptIds);
}
