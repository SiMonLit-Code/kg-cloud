package com.plantdata.kgcloud.domain.audit.repository;

import com.plantdata.kgcloud.domain.audit.entity.ApiAudit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface ApiAuditRepository extends JpaRepository<ApiAudit, Long> {

}
