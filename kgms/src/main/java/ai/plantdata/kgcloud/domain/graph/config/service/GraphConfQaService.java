package ai.plantdata.kgcloud.domain.graph.config.service;

import ai.plantdata.kgcloud.sdk.req.GraphConfQaReq;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfQaRsp;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfQaStatusRsp;

import java.util.List;

/**
 * @author jiangdeming
 * @date 2019/12/2
 */
public interface GraphConfQaService {
    /**
     * 保存qa问答
     *
     * @param reqs
     * @param kgName
     * @return
     */
    List<GraphConfQaRsp> saveQa(String kgName, List<GraphConfQaReq> reqs);


    /**
     * 查询qa问答
     *
     * @param kgName
     * @return
     */
    List<GraphConfQaRsp> findByKgName(String kgName);

    /**
     * 获取问答状态
     * @param kgName
     * @return
     */
    GraphConfQaStatusRsp getStatus(String kgName);

    /**
     * 更新问答状态
     * @param kgName
     * @param status
     */
    void updateStatus(String kgName, Integer status);
}
