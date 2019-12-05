package com.plantdata.kgcloud.domain.graph.attr.repository;

import com.plantdata.kgcloud.domain.graph.attr.entity.GraphAttrGroupDetails;
import com.plantdata.kgcloud.domain.graph.attr.entity.GraphAttrGroupDetailsPk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface GraphAttrGroupDetailsRepository extends JpaRepository<GraphAttrGroupDetails, GraphAttrGroupDetailsPk> {
    List<GraphAttrGroupDetails> findByGroupId(Long groupId);

    List<GraphAttrGroupDetails> findAllByGroupIdIn(List<Long> groupIdList);
}
