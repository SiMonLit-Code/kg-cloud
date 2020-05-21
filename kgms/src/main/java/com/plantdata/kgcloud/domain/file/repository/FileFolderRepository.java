package com.plantdata.kgcloud.domain.file.repository;

import com.plantdata.kgcloud.domain.file.entity.FileFolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author lp
 * @date 2020/5/20 15:23
 */
public interface FileFolderRepository extends JpaRepository<FileFolder, Long> {

}