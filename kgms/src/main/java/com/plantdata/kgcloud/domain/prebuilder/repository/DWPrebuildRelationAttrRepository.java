package com.plantdata.kgcloud.domain.prebuilder.repository;

import com.plantdata.kgcloud.domain.prebuilder.entity.DWPrebuildRelationAttr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DWPrebuildRelationAttrRepository extends JpaRepository<DWPrebuildRelationAttr, Integer> {


    @Modifying
    @Query(value = "delete from prebuild_relation_attr where model_id = :id",nativeQuery = true)
    void deleteByModelId(Integer id);
}
