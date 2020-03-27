package com.plantdata.kgcloud.domain.access.repository;

import com.plantdata.kgcloud.domain.access.entity.DWTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DWTaskRepository extends JpaRepository<DWTask, Integer> {

    List<DWTask> findAll(Specification<DWTask> spec);
}
