package com.plantdata.kgcloud.domain.model.service;

import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.sdk.req.KgmsModelReq;
import com.plantdata.kgcloud.sdk.req.ModelPageReq;
import com.plantdata.kgcloud.sdk.rsp.ModelRsp;
import org.springframework.data.domain.Page;

import java.util.List;


/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface ModelService {
    /**
     * 模型调用
     *
     * @param id
     * @param input
     * @return
     */
    Object call(Long id, List<String> input);


    /**
     * 查询所有接口
     *
     * @param userId
     * @return
     */
    List<ModelRsp> findAll(String userId);

    /**
     * 分页查询接口
     *
     * @param userId
     * @param baseReq
     * @return
     */
    Page<ModelRsp> findAll(String userId, ModelPageReq baseReq);

    /**
     * 根据id查询接口
     *
     * @param userId
     * @param id
     * @return
     */
    ModelRsp findById(String userId, Long id);

    /**
     * 删除
     *
     * @param userId
     * @param id
     */
    void delete(String userId, Long id);

    /**
     * 插入接口
     *
     * @param r
     * @return
     */
    ModelRsp insert(KgmsModelReq r);

    /**
     * 修改接口
     *
     * @param userId
     * @param id
     * @param r
     * @return
     */
    ModelRsp update(String userId, Long id, KgmsModelReq r);
}
