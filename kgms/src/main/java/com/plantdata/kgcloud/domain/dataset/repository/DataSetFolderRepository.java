package com.plantdata.kgcloud.domain.dataset.repository;

import com.plantdata.kgcloud.domain.dataset.entity.DataSetFolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface DataSetFolderRepository extends JpaRepository<DataSetFolder, Long> {
    /**
     * 查询数据集默认文件夹
     *
     * @param userId
     * @return
     */
    Optional<DataSetFolder> findByUserIdAndDefaultedIsTrue(String userId);

    /**
     * 查询用户下文件夹
     *
     * @param userId
     * @return
     */
    List<DataSetFolder> findByUserId(String userId);


    /**
     * 根据Id查询
     *
     * @param id
     * @param userId
     * @return
     */
    Optional<DataSetFolder> findByIdAndUserId(Long id, String userId);

    /**
     * 根据Id删除
     *
     * @param id
     * @param userId
     */
    void deleteByIdAndUserId(Long id, String userId);
}
