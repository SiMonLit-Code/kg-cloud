package com.plantdata.kgcloud.domain.dataset.service;

import com.plantdata.kgcloud.sdk.req.DataOptQueryReq;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-07 18:43
 **/
public interface DataOptService {
    /**
     * 分页
     *
     * @param datasetId
     * @param req
     * @return
     */
    Page<Map<String, Object>> getData(Long datasetId, DataOptQueryReq req);

    /**
     * 获取
     *
     * @param datasetId
     * @param dataId
     * @return
     */
    Map<String, Object> getDataById(Long datasetId, String dataId);


    /**
     * 新增
     *
     * @param datasetId
     * @param data
     * @return
     */
    Map<String, Object> insertData(Long datasetId, Map<String, Object> data);

    /**
     * 修改
     *
     * @param datasetId
     * @param dataId
     * @param data
     * @return
     */
    Map<String, Object> updateData(Long datasetId, String dataId, Map<String, Object> data);

    /**
     * 删除
     *
     * @param datasetId
     * @param dataId
     * @return
     */
    void deleteData(Long datasetId, String dataId);


    /**
     * 删除
     *
     * @param datasetId
     * @return
     */
    void deleteAll(Long datasetId);

    /**
     * 批量删除
     *
     * @param datasetId
     * @param dataIds
     * @return
     */
    void batchDeleteData(Long datasetId, Collection<String> dataIds);

    /**
     * 导出
     * @param datasetId
     * @param response
     */
    void exportData(Long datasetId, HttpServletResponse response);
}
