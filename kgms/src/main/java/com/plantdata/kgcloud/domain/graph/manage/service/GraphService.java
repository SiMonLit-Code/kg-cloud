package com.plantdata.kgcloud.domain.graph.manage.service;

import com.plantdata.kgcloud.domain.common.service.BaseService;
import com.plantdata.kgcloud.sdk.req.GraphPageReq;
import com.plantdata.kgcloud.sdk.req.GraphReq;
import com.plantdata.kgcloud.sdk.rsp.GraphRsp;
import org.springframework.data.domain.Page;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-08 10:02
 **/
public interface GraphService extends BaseService<GraphRsp, GraphReq, String> {
    /**
     * 按条件分页查询
     *
     * @param req
     * @return
     */
    Page<GraphRsp> findAll(String userId, GraphPageReq req);


    GraphRsp insert(String userId, GraphReq req);

    GraphRsp createDefault(String userId);
}
