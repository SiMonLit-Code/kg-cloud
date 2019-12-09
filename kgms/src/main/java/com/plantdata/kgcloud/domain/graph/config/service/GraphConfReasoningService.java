package com.plantdata.kgcloud.domain.graph.config.service;

import com.plantdata.kgcloud.domain.graph.config.req.GraphConfReasoningReq;
import com.plantdata.kgcloud.domain.graph.config.rsp.GraphConfReasoningRsp;
import com.plantdata.kgcloud.sdk.req.GraphConfKgqlReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfKgqlRsp;
import com.plantdata.kgcloud.sdk.rsp.GraphConfStatisticalRsp;

import java.util.List;

/**
 * Created by jdm on 2019/12/9 15:45.
 */
public interface GraphConfReasoningService {
    /**
     * 添加推理规则
     *
     * @param kgName
     * @param req
     * @return
     */
    GraphConfReasoningRsp createReasoning(String kgName , GraphConfReasoningReq req);

    /**
     * 根据id删除图谱推理
     * @param id
     */
    void deleteReasoning(Long id );

    /**
     * 查询所有
     * @return
     */
    List<GraphConfReasoningRsp> findAll();

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    GraphConfReasoningRsp findById(Long id);

    /**
     * 修改
     *
     * @param req
     * @return
     */
    GraphConfReasoningRsp updateReasoning(Long id, GraphConfReasoningReq req);
}
