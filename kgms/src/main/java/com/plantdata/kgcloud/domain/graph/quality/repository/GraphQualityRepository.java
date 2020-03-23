package com.plantdata.kgcloud.domain.graph.quality.repository;

import com.plantdata.kgcloud.domain.graph.quality.entity.GraphQuality;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: LinHo
 * @Date: 2020/3/21 14:21
 * @Description:
 */
public interface GraphQualityRepository extends JpaRepository<GraphQuality, Long> {
}
