package com.plantdata.kgcloud.domain.document.repository;

import com.plantdata.kgcloud.domain.document.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @description:
 * @author czj
 * @create: 2019-11-11 17:00
 **/
public interface DocumentRepository extends JpaRepository<Document,Integer> {

    Page<Document> findAll(Specification<Document> spec, Pageable pageable);


    @Modifying
    @Query(value="update Document  set doc_status = :status where scene_id = :sceneId and id = :id and doc_status < :status ",nativeQuery = true)
    void updateStatus(@Param("sceneId") Integer sceneId, @Param("id") Integer id, @Param("status") Integer status);
}
