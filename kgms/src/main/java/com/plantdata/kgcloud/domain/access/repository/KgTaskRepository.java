package com.plantdata.kgcloud.domain.access.repository;

import com.plantdata.kgcloud.domain.access.entity.KgTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KgTaskRepository extends JpaRepository<KgTask, Long> {
}
