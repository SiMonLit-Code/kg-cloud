package com.plantdata.kgcloud.domain.edit.repository;

import com.plantdata.kgcloud.domain.edit.entity.EntityFileRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author EYE
 */
public interface EntityFileRelationRepository extends JpaRepository<EntityFileRelation, Integer> {

    Page<EntityFileRelation> findAll(Specification<EntityFileRelation> spec, Pageable pageable);

    @Modifying
    @Query(value = "delete from entity_file_relation where dw_file_id = :dwFileId", nativeQuery = true)
    void deleteRelationByDwFileId(@Param("dwFileId") Integer dwFileId);

    @Modifying
    @Query(value = "delete from entity_file_relation where multi_modal_id = :multiModalId", nativeQuery = true)
    void deleteRelationByMultiModalId(@Param("multiModalId") String multiModalId);

    @Query(value = "select * from entity_file_relation where dw_file_id = :dwFileId", nativeQuery = true)
    EntityFileRelation getRelationByDwFileId(@Param("dwFileId") Integer dwFileId);

    @Query(value = "select * from entity_file_relation where multi_modal_id = :multiModalId", nativeQuery = true)
    EntityFileRelation getRelationByMultiModalId(@Param("multiModalId") String multiModalId);
}
