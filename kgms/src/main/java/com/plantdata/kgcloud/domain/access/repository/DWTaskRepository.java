package com.plantdata.kgcloud.domain.access.repository;

import com.plantdata.kgcloud.domain.access.entity.DWTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DWTaskRepository extends JpaRepository<DWTask, Integer> {
}
