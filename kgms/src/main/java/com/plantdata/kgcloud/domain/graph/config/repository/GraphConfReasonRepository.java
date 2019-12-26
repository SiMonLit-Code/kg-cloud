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

    /**
     * 分页
     *
     * @param kgName
     * @param pageable
     * @return
     */
    Page<GraphConfReasoning> getByKgName(String kgName, Pageable pageable);
}
