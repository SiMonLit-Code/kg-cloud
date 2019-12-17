package com.plantdata.kgcloud.domain.dataset.service;

import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import com.plantdata.kgcloud.sdk.req.DataSetCreateReq;
import com.plantdata.kgcloud.sdk.req.DataSetPageReq;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.sdk.req.DataSetSdkReq;
import com.plantdata.kgcloud.sdk.req.DataSetUpdateReq;
import com.plantdata.kgcloud.sdk.rsp.DataSetRsp;
import com.plantdata.kgcloud.sdk.rsp.DataSetUpdateRsp;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-06 10:42
 **/
public interface DataSetService {

    /**
     * 分页查询接口
     *
     * @param userId
     * @param req
     * @return
     */
    Page<DataSetRsp> findAll(String userId, DataSetPageReq req);


    /**
     *
     * @param dataNames
     * @return
     */
    List<Long> findByDataNames(String userId, List<String> dataNames);

    /**
     *
     * @param userId
     * @param database
     * @return
     */
    List<Long> findByDatabase(String userId,List<DataSetSdkReq> database);
    /**
     * 根据文件夹查询数据集
     *
     * @param id
     * @return
     */
    List<DataSet> findByFolderId(Long id);

    /**
     * 根据Id查询
     *
     * @param userId
     * @param id
     * @return
     */
    DataSet findOne(String userId, Long id);

    /**
     * 新增接口
     *
     * @param userId
     * @param req
     * @return
     */
    DataSetRsp insert(String userId, DataSetCreateReq req);

    /**
     * 数据集ids移动到文件夹
     *
     * @param userId
     * @param ids
     * @param folderId
     */
    void move(String userId, Collection<Long> ids, Long folderId);

    /**
     * 根据数据集Id批量删除
     *
     * @param userId
     * @param ids
     */
    void batchDelete(String userId, Collection<Long> ids);


    /**
     * 查询所有接口
     *
     * @param userId
     * @return
     */
    List<DataSetRsp> findAll(String userId);

    /**
     * 根据id查询接口
     *
     * @param userId
     * @param id
     * @return
     */
    DataSetUpdateRsp findById(String userId, Long id);

    /**
     * 根据id删除接口
     *
     * @param userId
     * @param id
     */
    void delete(String userId, Long id);

    /**
     * 修改接口
     *
     * @param userId
     * @param id
     * @param r
     * @return
     */
    DataSetRsp update(String userId, Long id, DataSetUpdateReq r);

    /**
     * schema识别
     *
     * @param dataType
     * @param file
     * @return
     */
    List<DataSetSchema> schemaResolve(Integer dataType, MultipartFile file);

}
