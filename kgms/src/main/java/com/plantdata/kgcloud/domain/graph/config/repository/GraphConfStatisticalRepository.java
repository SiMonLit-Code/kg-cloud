package com.plantdata.kgcloud.domain.graph.config.repository;

import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfStatistical;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface GraphConfStatisticalRepository extends JpaRepository<GraphConfStatistical, Long> {


    /**
     * 查询
     * @param kgName
     * @return
     */
    List<GraphConfStatistical> findByKgName(String kgName);

    /**
     * 分页
     * @param kgName
     * @param pageable
     * @return
     */
    Page<GraphConfStatistical> getByKgName(String kgName, Pageable pageable);



}
