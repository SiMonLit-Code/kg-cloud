package com.plantdata.kgcloud.domain.access.repository;

import com.plantdata.kgcloud.domain.access.entity.EtlTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EtlTaskRepository extends JpaRepository<EtlTask, Long> {
}
