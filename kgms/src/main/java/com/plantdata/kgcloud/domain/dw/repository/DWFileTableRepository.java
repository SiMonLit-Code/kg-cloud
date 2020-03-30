package com.plantdata.kgcloud.domain.dw.repository;

import com.plantdata.kgcloud.domain.dw.entity.DWDatabase;
import com.plantdata.kgcloud.domain.dw.entity.DWFileTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DWFileTableRepository  extends JpaRepository<DWFileTable, Integer> {

    Page<DWFileTable> findAll(Specification<DWFileTable> spec, Pageable pageable);
}
