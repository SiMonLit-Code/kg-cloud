package com.plantdata.kgcloud.domain.dataset.service;

import com.plantdata.kgcloud.domain.dataset.provider.DataOptProvider;
import com.plantdata.kgcloud.sdk.req.DataOptQueryReq;
import com.plantdata.kgcloud.sdk.req.app.dataset.DataSetAddReq;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
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
     * @param userId
     * @param datasetId
     * @param req
     * @return
     */
    Page<Map<String, Object>> getData(String userId, Long datasetId, DataOptQueryReq req);

    /**
     * 获取
     *
     * @param userId
     * @param datasetId
     * @param dataId
     * @return
     */
    Map<String, Object> getDataById(String userId, Long datasetId, String dataId);


    /**
     * 新增
     *
     * @param userId
     * @param datasetId
     * @param data
     * @return
     */
    Map<String, Object> insertData(String userId, Long datasetId, Map<String, Object> data);


    /**
     * 文件上传
     *
     * @param userId
     * @param datasetId
     * @param file
     * @throws Exception
     */
    void upload(String userId, Long datasetId, MultipartFile file) throws Exception;

    /**
     * 修改
     *
     * @param userId
     * @param datasetId
     * @param dataId
     * @param data
     * @return
     */
    Map<String, Object> updateData(String userId, Long datasetId, String dataId, Map<String, Object> data);

    /**
     * 批量插入
     *
     * @param userId
     * @param datasetId
     * @param dataList
     */
    void batchInsertData(String userId, Long datasetId, List<Map<String, Object>> dataList);

    /**
     * 删除
     *
     * @param userId
     * @param datasetId
     * @param dataId
     */
    void deleteData(String userId, Long datasetId, String dataId);


    /**
     * 删除全部
     *
     * @param userId
     * @param datasetId
     */
    void deleteAll(String userId, Long datasetId);

    /**
     * 批量删除
     *
     * @param userId
     * @param datasetId
     * @param dataIds
     */
    void batchDeleteData(String userId, Long datasetId, Collection<String> dataIds);

    /**
     * 导出
     *
     * @param userId
     * @param datasetId
     * @param response
     * @throws Exception
     */
    void exportData(String userId, Long datasetId, HttpServletResponse response) throws Exception;

    /**
     * 获取 Provider
     * @param userId
     * @param datasetId
     * @return
     */
    DataOptProvider getProvider(String userId, Long datasetId);

    /**
     * 批量新增数据集
     *
     * @param userId
     * @param addReq
     */
    void batchAddDataForDataSet(String userId, DataSetAddReq addReq);
}
