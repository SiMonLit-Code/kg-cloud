package ai.plantdata.kgcloud.domain.graph.config.repository;

import ai.plantdata.kgcloud.domain.graph.config.entity.GraphConfFocus;
import ai.plantdata.kgcloud.domain.graph.config.entity.GraphConfFocusPk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface GraphConfFocusRepository extends JpaRepository<GraphConfFocus, GraphConfFocusPk> {
    /**
     * 根据kgName查询
     *
     * @param kgName
     * @return
     */
    Optional<List<GraphConfFocus>> findByKgName(String kgName);

    /**
     * 根据kgName 和 type查询
     *
     * @param kgName
     * @param type
     * @return
     */
    Optional<GraphConfFocus> findByKgNameAndType(String kgName, String type);
}
