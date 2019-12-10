package com.plantdata.kgcloud.domain.graph.config.service;

import com.plantdata.kgcloud.sdk.req.GraphConfStatisticalReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfStatisticalRsp;

import java.util.List;

/**
 * Created by plantdata-1007 on 2019/12/3.
 */
public interface GraphConfStatisticalService {
    /**
     * 创建统计
     *
     * @param kgName
     * @param req
     * @return
     */

    GraphConfStatisticalRsp createStatistical(String kgName, GraphConfStatisticalReq req);

    /**
     * 批量创建
     * @param listReq
     * @return
     */
    List<GraphConfStatisticalRsp> saveAll(String kgName ,List<GraphConfStatisticalReq> listReq);




    /**
     * 修改统计
     *
     * @param req
     * @return
     */
    GraphConfStatisticalRsp updateStatistical(Long id, GraphConfStatisticalReq req);

    /**
     * 批量修改
     * @param kgName
     * @param reqs
     * @return
     */
    List<GraphConfStatisticalRsp> updateAll(String kgName , List<GraphConfStatisticalReq> reqs);



    /**
     * 删除统计
     *
     * @param id
     */
    void deleteStatistical(Long id);

    /**
     * 批量删除统计
     *
     * @param ids
     */
    void deleteInBatch(List<Long> ids);


    /**
     * 通过kgName查询统计
     *
     * @param kgName
     * @return
     */
    List<GraphConfStatisticalRsp> findByKgName(String kgName);

    /**
     * 查询所有
     *
     * @return
     */
    List<GraphConfStatisticalRsp> findAll();
}
