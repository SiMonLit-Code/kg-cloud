package com.plantdata.kgcloud.domain.dataset.repository;

import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface DataSetRepository extends JpaRepository<DataSet, Long> {
    /**
     * 根据用户和id删除数据集
     *
     * @param id
     * @param userId
     */
    void deleteByUserIdAndId(String userId, Long id);

    /**
     * 根据用户和id查询数据集
     *
     * @param id
     * @param userId
     * @return
     */
    Optional<DataSet> findByUserIdAndId(String userId, Long id);


    Optional<DataSet> findByDataName(String dataName);

    /**
     * 根据文件夹查询
     *
     * @param folderId
     * @return
     */
    List<DataSet> findByFolderId(Long folderId);
}
