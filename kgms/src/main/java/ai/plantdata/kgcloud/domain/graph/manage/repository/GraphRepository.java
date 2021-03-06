package ai.plantdata.kgcloud.domain.graph.manage.repository;

import ai.plantdata.kgcloud.domain.graph.manage.entity.Graph;
import ai.plantdata.kgcloud.domain.graph.manage.entity.GraphPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface GraphRepository extends JpaRepository<Graph, GraphPk>, JpaSpecificationExecutor<Graph> {

    Graph findByKgNameAndUserId(String kgName, String userId);

    Graph findByDbName(String dbName);
}
