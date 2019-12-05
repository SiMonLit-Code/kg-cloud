package com.plantdata.kgcloud.domain.audit;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface ApiAuditRepository extends JpaRepository<ApiAudit, Long> {

}
