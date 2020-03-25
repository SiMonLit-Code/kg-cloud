package com.plantdata.kgcloud.domain.data.service;

import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.domain.data.req.DataStoreModifyReq;
import com.plantdata.kgcloud.domain.data.req.DataStoreScreenReq;
import com.plantdata.kgcloud.domain.data.req.DtReq;
import com.plantdata.kgcloud.domain.data.rsp.DataStoreRsp;
import com.plantdata.kgcloud.domain.data.rsp.DbAndTableRsp;

import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2020/3/24 15:21
 * @Description:
 */
public interface DataStoreService {

    /**
     * 数据库与表列表
     *
     * @param dtReq
     * @return
     */
    List<DbAndTableRsp> listAll(DtReq dtReq);

    /**
     * 数仓列表
     *
     * @param req
     * @return
     */
    BasePage<DataStoreRsp> listDataStore(DataStoreScreenReq req);

    /**
     * 修正错误数据
     *
     * @param modifyReq
     */
    void updateData(DataStoreModifyReq modifyReq);

    /**
     * 删除数据
     *
     * @param id
     */
    void deleteData(String id);

    /**
     * 批量发回数仓
     *
     * @param ids
     */
    void sendData(List<String> ids);
}
