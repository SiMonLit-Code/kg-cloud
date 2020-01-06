package com.plantdata.kgcloud.domain.dataset.repository;

import com.plantdata.kgcloud.domain.dataset.entity.DataSetAnnotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface DataSetAnnotationRepository extends JpaRepository<DataSetAnnotation, Long> , JpaSpecificationExecutor<DataSetAnnotation> {
}
