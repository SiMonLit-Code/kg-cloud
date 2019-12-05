package com.plantdata.kgcloud.domain.common.repository;

import com.plantdata.kgcloud.domain.common.entity.SystemLimit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface SystemLimitRepository extends JpaRepository<SystemLimit, String> {
}
