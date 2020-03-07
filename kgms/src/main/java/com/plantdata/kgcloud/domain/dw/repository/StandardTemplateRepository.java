package com.plantdata.kgcloud.domain.dw.repository;

import com.plantdata.kgcloud.domain.dw.entity.DWStandardTemplate;
import com.plantdata.kgcloud.domain.dw.entity.DWTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StandardTemplateRepository extends JpaRepository<DWStandardTemplate, Long> {
}
