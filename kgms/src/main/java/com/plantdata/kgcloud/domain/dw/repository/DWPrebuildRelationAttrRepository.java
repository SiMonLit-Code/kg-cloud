package com.plantdata.kgcloud.domain.dw.repository;

import com.plantdata.kgcloud.domain.dw.entity.DWPrebuildRelationAttr;
import org.apache.ibatis.annotations.Delete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DWPrebuildRelationAttrRepository extends JpaRepository<DWPrebuildRelationAttr, Integer> {


    @Modifying
    @Query(value = "delete from dw_prebuild_relation_attr where model_id = :id",nativeQuery = true)
    void deleteByModelId(Integer id);
}
