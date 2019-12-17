package com.plantdata.kgcloud.domain.task.repository;

import com.plantdata.kgcloud.domain.task.entity.TaskGraphStatus;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: LinHo
 * @Date: 2019/12/16 17:43
 * @Description:
 */
public interface TaskGraphStatusRepository extends JpaRepository<TaskGraphStatus,Long> {
}
