package com.plantdata.kgcloud.domain.model.service;

import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.sdk.req.KgmsModelReq;
import com.plantdata.kgcloud.sdk.rsp.ModelRsp;
import com.plantdata.kgcloud.domain.common.service.BaseService;
import com.plantdata.kgcloud.sdk.req.WordReq;
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
     * @return
     */
    List<ModelRsp> findAll(String userId);

    /**
     * 分页查询接口
     *
     * @return
     */
    Page<ModelRsp> findAll(String userId, BaseReq baseReq);

    /**
     * 根据id查询接口
     *
     * @param id
     * @return
     */
    ModelRsp findById(String userId, Long id);

    /**
     * 根据id删除接口
     *
     * @param id
     * @return
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
     * @param id
     * @param r
     * @return
     */
    ModelRsp update(String userId, Long id, KgmsModelReq r);
}
