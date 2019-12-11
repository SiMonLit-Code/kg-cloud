package com.plantdata.kgcloud.domain.graph.config.repository;

import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfReasoning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface GraphConfReasonRepository extends JpaRepository<GraphConfReasoning, Long> {

    Page<GraphConfReasoning> findByKgName(String kgName, Pageable pageable);
}
