package ai.plantdata.kgcloud.domain.graph.config.repository;

import ai.plantdata.kgcloud.domain.graph.config.entity.GraphConfSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Bovin
 * @description
 * @since 2020-08-12 16:48
 **/
public interface GraphConfSnapshotRepository extends JpaRepository<GraphConfSnapshot, Long> {
}
