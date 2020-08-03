package ai.plantdata.kgcloud.domain.edit.repository;

import ai.plantdata.kgcloud.domain.edit.entity.TaskGraphStatus;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: LinHo
 * @Date: 2019/12/16 17:43
 * @Description:
 */
public interface TaskGraphStatusRepository extends JpaRepository<TaskGraphStatus,Long> {
}
