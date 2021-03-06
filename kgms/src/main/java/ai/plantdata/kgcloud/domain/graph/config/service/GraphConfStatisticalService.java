package ai.plantdata.kgcloud.domain.graph.config.service;

import ai.plantdata.cloud.bean.BasePage;
import ai.plantdata.cloud.bean.BaseReq;
import ai.plantdata.kgcloud.sdk.req.GraphConfStatisticalReq;
import ai.plantdata.kgcloud.sdk.req.UpdateGraphConfStatisticalReq;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfStatisticalRsp;

import java.util.List;

/**
 * @author jiangdeming
 * @date 2019/12/3
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
     *
     * @param listReq
     * @return
     */
    List<GraphConfStatisticalRsp> saveAll(List<GraphConfStatisticalReq> listReq);


    /**
     * 修改统计
     *
     * @param id
     * @param req
     * @return
     */
    GraphConfStatisticalRsp updateStatistical(Long id, GraphConfStatisticalReq req);

    /**
     * 批量修改
     *
     * @param
     * @param reqs
     * @return
     */
    List<GraphConfStatisticalRsp> updateAll(List<UpdateGraphConfStatisticalReq> reqs);


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
     * 分页
     *
     * @param kgName
     * @param baseReq
     * @return
     */
    BasePage<GraphConfStatisticalRsp> getByKgName(String kgName, BaseReq baseReq);
}
