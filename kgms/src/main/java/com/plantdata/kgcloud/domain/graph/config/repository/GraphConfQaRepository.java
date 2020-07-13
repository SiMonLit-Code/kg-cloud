package com.plantdata.kgcloud.domain.graph.config.repository;

import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfQa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface GraphConfQaRepository extends JpaRepository<GraphConfQa, Long> {
    /**
     * 根据图谱名删除问答
     *
     * @param
     */
    void deleteByKgName(String kgName);
    void deleteAllByKgName(String kgName);
    List<GraphConfQa> findAllByKgName(String kgName);
}
