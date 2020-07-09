package com.plantdata.kgcloud.domain.graph.config.service;

import ai.plantdata.cloud.bean.BasePage;
import ai.plantdata.cloud.bean.BaseReq;
import com.plantdata.kgcloud.sdk.req.GraphConfReasonReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfReasonRsp;
import org.springframework.data.domain.Page;

/**
 * @author jdm
 * @date 2019/12/9 15:45
 */
public interface GraphConfReasonService {
    /**
     * 添加推理规则
     *
     * @param kgName
     * @param req
     * @return
     */
    GraphConfReasonRsp createReasoning(String kgName, GraphConfReasonReq req);

    /**
     * 根据id删除图谱推理
     *
     * @param id
     */
    void deleteReasoning(Long id);

    /**
     * 分页
     *
     * @param kgName
     * @param baseReq
     * @return
     */
    BasePage<GraphConfReasonRsp> getByKgName(String kgName, BaseReq baseReq);

    /**
     * 根据id查询详情
     *
     * @param id
     * @return
     */
    GraphConfReasonRsp findById(Long id);

    /**
     * 修改
     *
     * @param id
     * @param req
     * @return
     */
    GraphConfReasonRsp updateReasoning(Long id, GraphConfReasonReq req);
}
