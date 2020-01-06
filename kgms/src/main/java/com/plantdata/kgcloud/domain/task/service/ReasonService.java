package com.plantdata.kgcloud.domain.task.service;


import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.domain.task.dto.ReasonBean;

import java.util.List;
import java.util.Map;

public interface ReasonService {

    /**
     * 结果分页
     * @param kgName
     * @param execId
     * @param req
     * @return
     */
    ReasonBean listByPage(String kgName, Integer execId, BaseReq req);

    /**
     * 入图
     *
     * @param type
     * @param kgName
     * @param mode
     * @param taskId
     * @param dataIdList
     * @return
     */
    Map<String, Object> importTriple(int type, String kgName, int mode, Integer taskId, List<String> dataIdList);

}