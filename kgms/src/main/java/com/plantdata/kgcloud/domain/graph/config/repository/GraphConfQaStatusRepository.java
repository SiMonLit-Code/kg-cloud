package com.plantdata.kgcloud.domain.graph.config.repository;

import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfQaStatus;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author lp
 * @date 2020/5/21 17:15
 */
public interface GraphConfQaStatusRepository extends JpaRepository<GraphConfQaStatus, Long> {

}
