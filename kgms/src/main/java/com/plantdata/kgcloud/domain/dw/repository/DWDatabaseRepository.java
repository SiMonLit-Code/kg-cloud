package com.plantdata.kgcloud.domain.dw.repository;

import com.plantdata.kgcloud.domain.dw.entity.DWDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/

public interface DWDatabaseRepository extends JpaRepository<DWDatabase, Long> {


    /**
     * @Description: 根据查询条件跟分页条件查询数据
     * @Param:
     * @return:
     * @Author: czj
     * @Date: 2020/3/13
     */
    Page<DWDatabase> findAll(Specification<DWDatabase> spec, Pageable pageable);

    DWDatabase findByDataName(String dbName);

    List<DWDatabase> findByUserId(String userId);
}
