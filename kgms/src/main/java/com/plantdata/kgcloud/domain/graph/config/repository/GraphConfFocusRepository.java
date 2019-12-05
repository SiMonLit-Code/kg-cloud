package com.plantdata.kgcloud.domain.graph.config.repository;

import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfFocus;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfFocusPk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface GraphConfFocusRepository extends JpaRepository<GraphConfFocus, GraphConfFocusPk> {
    Optional<List<GraphConfFocus>> findByKgName(String kgName);
}
