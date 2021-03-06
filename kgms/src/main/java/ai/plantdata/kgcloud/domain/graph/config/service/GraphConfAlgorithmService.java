package ai.plantdata.kgcloud.domain.graph.config.service;

import ai.plantdata.kgcloud.sdk.req.GraphConfAlgorithmReq;
import ai.plantdata.kgcloud.sdk.req.GraphConfAlgorithmReqList;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfAlgorithmRsp;
import org.springframework.data.domain.Page;

/**
 * @author jiangdeming
 * @date 2019/11/29
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
    GraphConfAlgorithmRsp createAlgorithm(String kgName, GraphConfAlgorithmReq req);

    /**
     * 修改算法
     *
     * @param id
     * @param req
     * @return
     */
    GraphConfAlgorithmRsp updateAlgorithm(Long id, GraphConfAlgorithmReq req);

    /**
     * 删除算法
     *
     * @param id
     */
    void deleteAlgorithm(Long id);

    /**
     * 查询算法
     *
     * @param kgName
     * @param baseReq
     * @return
     */
    Page<GraphConfAlgorithmRsp> findByKgName(String kgName, GraphConfAlgorithmReqList baseReq);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    GraphConfAlgorithmRsp findById(Long id);
}
