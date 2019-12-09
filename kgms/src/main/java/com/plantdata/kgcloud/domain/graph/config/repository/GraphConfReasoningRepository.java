package com.plantdata.kgcloud.domain.graph.config.repository;

import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfReasoning;
import com.plantdata.kgcloud.domain.graph.config.rsp.GraphConfReasoningRsp;
import com.plantdata.kgcloud.sdk.rsp.GraphConfKgqlRsp;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface GraphConfReasoningRepository extends JpaRepository<GraphConfReasoning, Long> {
    GraphConfReasoningRsp finById(Long id);

}
