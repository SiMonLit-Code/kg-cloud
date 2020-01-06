package com.plantdata.kgcloud.domain.graph.config.service;

import com.plantdata.kgcloud.sdk.req.GraphConfQaReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfQaRsp;

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
}
