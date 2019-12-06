package com.plantdata.kgcloud.domain.document.repository;

import com.plantdata.kgcloud.domain.document.entity.Concept;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConceptRepository  extends JpaRepository<Concept,Integer> {
}
