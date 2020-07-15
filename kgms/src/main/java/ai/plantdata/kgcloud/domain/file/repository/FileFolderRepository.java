package ai.plantdata.kgcloud.domain.file.repository;

import ai.plantdata.kgcloud.domain.file.entity.FileFolder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author lp
 * @date 2020/5/20 15:23
 */
public interface FileFolderRepository extends JpaRepository<FileFolder, Long> {

}