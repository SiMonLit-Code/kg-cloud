package com.plantdata.kgcloud.domain.reasoning.repository;

import com.plantdata.kgcloud.domain.reasoning.entity.Reasoning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReasoningRepository extends JpaRepository<Reasoning, Integer> {

    Page<Reasoning> findAll(Specification<Reasoning> specification, PageRequest pageable);
}
