package com.plantdata.kgcloud.domain.access.repository;

import com.plantdata.kgcloud.domain.access.entity.DWTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DWTaskRepository extends JpaRepository<DWTask, Integer>, JpaSpecificationExecutor<DWTask> {

    List<DWTask> findAll(Specification<DWTask> spec);

    List<DWTask> findAllByNameIn(List<String> transfTasks);
}
