package com.plantdata.kgcloud.domain.dataset.service;

import com.plantdata.kgcloud.domain.common.service.BaseService;
import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import com.plantdata.kgcloud.sdk.req.DataSetPageReq;
import com.plantdata.kgcloud.sdk.req.DataSetReq;
import com.plantdata.kgcloud.sdk.rsp.DataSetRsp;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-06 10:42
 **/
public interface DataSetService extends BaseService<DataSetRsp, DataSetReq, Long> {

    /**
     * 分页查询接口
     *
     * @param req
     * @return
     */
    Page<DataSetRsp> findAll(String userId, DataSetPageReq req);

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
     * @param id
     * @return
     */
    DataSet findOne(String userId, Long id);

    DataSet findOne(Long id);

    DataSetRsp insert(String userId, DataSetReq req);

    /**
     * 数据集ids移动到文件夹
     *
     * @param ids
     * @param folderId
     * @return
     */
    void move(String userId, Collection<Long> ids, Long folderId);

    /**
     * 根据数据集Id批量删除
     *
     * @param ids
     */
    void batchDelete(String userId, Collection<Long> ids);
}
