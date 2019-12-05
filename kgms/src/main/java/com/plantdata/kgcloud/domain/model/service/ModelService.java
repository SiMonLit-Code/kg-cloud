package com.plantdata.kgcloud.domain.model.service;

import com.plantdata.kgcloud.sdk.rsp.ModelRsp;
import com.plantdata.kgcloud.domain.common.service.BaseService;
import com.plantdata.kgcloud.sdk.req.WordReq;

import java.util.List;


/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface ModelService extends BaseService<ModelRsp, WordReq.ModelReq, Long> {
    /**
     * 模型调用
     *
     * @param id
     * @param input
     * @return
     */
    Object call(Long id, List<String> input);
}
