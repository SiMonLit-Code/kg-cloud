package com.plantdata.kgcloud.domain.graph.config.repository;

import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfKgql;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface GraphConfKgqlRepository extends JpaRepository<GraphConfKgql, Long> {
    /**
     * 查询
     *
     * @param kgName
     * @return
     */
    List<GraphConfKgql> findByKgName(String kgName);

    /**
     * 分页
     *
     * @param kgName
     * @param ruleType
     * @param pageable
     * @return
     */
    Page<GraphConfKgql> findByKgNameAndRuleType(String kgName, Integer ruleType, Pageable pageable);
}
