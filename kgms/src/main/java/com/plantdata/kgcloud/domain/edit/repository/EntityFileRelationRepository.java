package com.plantdata.kgcloud.domain.edit.repository;

import com.plantdata.kgcloud.domain.edit.entity.EntityFileRelation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

/**
 * @author EYE
 */
public interface EntityFileRelationRepository extends JpaRepository<EntityFileRelation, Integer> {

    Page<EntityFileRelation> findAll(Specification<EntityFileRelation> spec, Pageable pageable);

    Page<EntityFileRelation> getByKgNameAndNameContaining(String kgName, String name, Pageable pageable);

    @Modifying
    void deleteByDwFileId(Integer dwFileId);

    List<EntityFileRelation> getByDwFileId(Integer dwFileId);

    List<EntityFileRelation> getByKgNameAndEntityId(String kgName, Long entityId);

    List<EntityFileRelation> getByKgNameAndEntityIdIn(String kgName, List<Long> entityIds);
}
