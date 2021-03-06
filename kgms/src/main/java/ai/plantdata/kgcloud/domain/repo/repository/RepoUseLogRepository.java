package ai.plantdata.kgcloud.domain.repo.repository;

import ai.plantdata.kgcloud.domain.repo.entity.RepoUseLog;
import ai.plantdata.kgcloud.domain.repo.enums.RepositoryLogEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author cjw
 * @date 2020/5/18  15:06
 */
public interface RepoUseLogRepository extends JpaRepository<RepoUseLog, Integer> {

    List<RepoUseLog> deleteByClickIdInAndLogType(List<Integer> businessId, RepositoryLogEnum type);

    List<RepoUseLog> findAllByUserIdAndLogType(String userId, RepositoryLogEnum type);
}
