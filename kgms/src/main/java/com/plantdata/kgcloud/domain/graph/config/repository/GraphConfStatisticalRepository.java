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



    List<GraphConfStatistical> findByKgName(String kgName);

    Page<GraphConfStatistical> getByKgName(String kgName, Pageable pageable);



}
