package com.plantdata.kgcloud.domain.graph.config.service;

import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.sdk.req.GraphConfKgqlReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfKgqlRsp;
import org.springframework.data.domain.Page;

/**
 * 图谱业务配置
 *
 * @author jiangdeming
 * @date 2019/12/2
 */
public interface GraphConfKgqlService {

    /**
     * 创建kgql业务
     *
     * @param req
     * @param kgName
     * @return
     */
    GraphConfKgqlRsp createKgql(String kgName,GraphConfKgqlReq req);

    /**
     * 修改kgql业务
     * @param id
     * @param req
     * @return
     */
    GraphConfKgqlRsp updateKgql(Long id, GraphConfKgqlReq req);

    /**
     * 删除kgql业务
     *
     * @param id
     * @return
     */
    void deleteKgql(Long id);

    /**
     * 分页
     * @param kgName
     * @param ruleType
     * @param baseReq
     * @return
     */
    Page<GraphConfKgqlRsp> findByKgNameAndRuleType(String kgName ,Integer ruleType, BaseReq baseReq );


    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    GraphConfKgqlRsp findById(Long id);
}
