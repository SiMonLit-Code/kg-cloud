package com.plantdata.kgcloud.domain.graph.task.repository;

import com.plantdata.kgcloud.domain.graph.task.entity.TaskGraphSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface TaskGraphSnapshotRepository extends JpaRepository<TaskGraphSnapshot, Long> {
}
