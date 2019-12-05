package com.plantdata.kgcloud.domain.graph.manage.repository;

import com.plantdata.kgcloud.domain.graph.manage.entity.Graph;
import com.plantdata.kgcloud.domain.graph.manage.entity.GraphPk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface GraphRepository extends JpaRepository<Graph, GraphPk> {

    /**
     * 查询
     *
     * @param userId
     * @param kw
     * @param pageable
     * @return
     */
    Page<Graph> findByUserIdAndTitleContaining(String userId, String kw, Pageable pageable);
}
