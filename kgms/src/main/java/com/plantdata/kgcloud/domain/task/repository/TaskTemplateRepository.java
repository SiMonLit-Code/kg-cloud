package com.plantdata.kgcloud.domain.task.repository;

import com.plantdata.kgcloud.domain.task.entity.TaskTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface TaskTemplateRepository extends JpaRepository<TaskTemplate, Long> {
}
