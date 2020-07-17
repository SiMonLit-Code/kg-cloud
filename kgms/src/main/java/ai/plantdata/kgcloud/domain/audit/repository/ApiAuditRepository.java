package ai.plantdata.kgcloud.domain.audit.repository;

import ai.plantdata.kgcloud.domain.audit.entity.ApiAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface ApiAuditRepository extends JpaRepository<ApiAudit, Long>, JpaSpecificationExecutor<ApiAudit> {

}
