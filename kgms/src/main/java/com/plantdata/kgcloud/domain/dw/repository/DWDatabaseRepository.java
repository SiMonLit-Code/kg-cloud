package com.plantdata.kgcloud.domain.dw.repository;

import com.plantdata.kgcloud.domain.dw.entity.DWDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/

public interface DWDatabaseRepository extends JpaRepository<DWDatabase, Long> {

    Page<DWDatabase> findAll(Specification<DWDatabase> spec, Pageable pageable);
}
