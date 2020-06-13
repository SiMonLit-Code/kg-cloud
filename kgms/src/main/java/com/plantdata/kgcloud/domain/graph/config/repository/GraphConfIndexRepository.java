package com.plantdata.kgcloud.domain.graph.config.repository;

import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfIndex;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfQa;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface GraphConfIndexRepository extends JpaRepository<GraphConfIndex, Long> {
}
