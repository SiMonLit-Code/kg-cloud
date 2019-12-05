package com.plantdata.kgcloud.domain.graph.attr.repository;

import com.plantdata.kgcloud.domain.graph.attr.entity.GraphAttrGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface GraphAttrGroupRepository extends JpaRepository<GraphAttrGroup, Long> {
    List<GraphAttrGroup> findAllByKgName(String kgName);
}
