package com.plantdata.kgcloud.domain.file.repository;

import com.plantdata.kgcloud.domain.file.entity.FileTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author lp
 * @date 2020/5/20 15:23
 */
public interface FileTableRepository extends JpaRepository<FileTable, Long> {

    List<FileTable> findByIdIn(List<Long> tableIds);
    
}