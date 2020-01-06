package com.plantdata.kgcloud.domain.graph.manage.service;

import com.plantdata.kgcloud.sdk.req.GraphPageReq;
import com.plantdata.kgcloud.sdk.req.GraphReq;
import com.plantdata.kgcloud.sdk.rsp.GraphRsp;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-08 10:02
 **/
public interface GraphService {
    /**
     * 按条件分页查询
     *
     * @param req
     * @return
     */
    Page<GraphRsp> findAll(String userId, GraphPageReq req);

    /**
     * 查询所有接口
     *
     * @return
     */
    List<GraphRsp> findAll(String userId);

    /**
     * 新增图谱
     *
     * @param userId
     * @param req
     * @return
     */
    GraphRsp insert(String userId, GraphReq req);

    /**
     * 创建默认图谱
     *
     * @param userId
     * @return
     */
    GraphRsp createDefault(String userId);


    /**
     * 根据id查询接口
     *
     * @param id
     * @return
     */
    GraphRsp findById(String userId, String id);

    /**
     * 根据id删除接口
     *
     * @param id
     * @return
     */
    void delete(String userId, String id);

    /**
     * 修改接口
     *
     * @param userId
     * @param id
     * @param r
     * @return
     */
    GraphRsp update(String userId, String id, GraphReq r);


    String getDbName(String kgName);

}
