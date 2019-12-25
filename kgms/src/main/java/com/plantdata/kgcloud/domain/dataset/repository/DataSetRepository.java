package com.plantdata.kgcloud.domain.dataset.repository;

import com.plantdata.kgcloud.sdk.constant.DataType;
import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface DataSetRepository extends JpaRepository<DataSet, Long>, JpaSpecificationExecutor<DataSet> {
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

    /**
     *
     * @param dataName
     * @return
     */
    Optional<DataSet> findByDataName(String dataName);


    /**
     * s
     * @param userId
     * @param dataNames
     * @return
     */
    List<DataSet> findByUserIdAndDataNameIn(String userId,List<String> dataNames);


    /**
     * s
     * @param userId
     * @param dbName
     * @param tbName
     * @return
     */
    Optional<DataSet> findByUserIdAndDbNameAndTbName(String userId,String dbName,String tbName);

    /**
     * 根据文件夹查询
     *
     * @param folderId
     * @return
     */
    List<DataSet> findByFolderId(Long folderId);

    List<DataSet> findByDataTypeAndDataName(DataType dataType, String dataName);
}
