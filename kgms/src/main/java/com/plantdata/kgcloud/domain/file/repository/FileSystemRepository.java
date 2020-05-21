package com.plantdata.kgcloud.domain.file.repository;

import com.plantdata.kgcloud.domain.file.entity.FileSystem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author lp
 * @date 2020/5/20 14:53
 */
public interface FileSystemRepository extends JpaRepository<FileSystem, Long> {

    /**
     * 根据查询条件跟分页条件查询数据
     * @param spec
     * @param pageable
     * @return
     */
    Page<FileSystem> findAll(Specification<FileSystem> spec, Pageable pageable);

    List<FileSystem> findByUserId(String userId);
}
