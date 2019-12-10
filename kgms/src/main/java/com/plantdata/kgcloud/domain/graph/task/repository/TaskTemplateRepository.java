package com.plantdata.kgcloud.domain.graph.task.repository;

import com.plantdata.kgcloud.domain.graph.task.entity.TaskTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface TaskTemplateRepository extends JpaRepository<TaskTemplate, Long> {
}
