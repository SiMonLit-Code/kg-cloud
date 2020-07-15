package ai.plantdata.kgcloud.domain.graph.config.repository;

import ai.plantdata.kgcloud.domain.graph.config.entity.GraphConfIndex;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface GraphConfIndexRepository extends JpaRepository<GraphConfIndex, Long> {
}
