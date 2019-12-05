package com.plantdata.kgcloud.domain.graph.config.repository;

import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfAlgorithm;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface GraphConfAlgorithmRepository extends JpaRepository<GraphConfAlgorithm, Long> {

    List<GraphConfAlgorithm> findByKgName(String kgName);

    /**
     * 分页
     *
     * @return
     */
    Page<GraphConfAlgorithm> findByKgName(String kgName, Pageable pageable);
}
