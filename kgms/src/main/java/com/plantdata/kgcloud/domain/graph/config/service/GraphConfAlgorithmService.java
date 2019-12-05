package com.plantdata.kgcloud.domain.graph.config.service;

import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.sdk.req.GraphConfAlgorithmReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfAlgorithmRsp;
import org.springframework.data.domain.Page;

/**
 *
 * Created by plantdata-1007 on 2019/11/29.
 */
public interface GraphConfAlgorithmService {
    /**
     * \
     * 创建算法
     *
     * @param kgName
     * @param req
     * @return
     */
    GraphConfAlgorithmRsp createAlgorithm(String kgName,GraphConfAlgorithmReq req);

    /**
     * 修改算法
     *
     * @param req
     * @return
     */
    GraphConfAlgorithmRsp updateAlgorithm(Long id, GraphConfAlgorithmReq req);

    /**
     * 删除算法
     *
     * @param id
     */
    void deleteAlgorithm(Long id );

    /**
     * 查询算法
     *
     * @param kgName
     * @return
     */
    Page<GraphConfAlgorithmRsp> findByKgName(String kgName , BaseReq baseReq);


}
