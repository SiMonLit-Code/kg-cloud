package com.plantdata.kgcloud.domain.dw.service;

import com.alibaba.fastjson.JSONObject;
import com.plantdata.kgcloud.domain.dw.req.GraphMapReq;
import com.plantdata.kgcloud.domain.dw.rsp.GraphMapRsp;

import java.util.List;

public interface GraphMapService {
    List<GraphMapRsp> list(String userId, GraphMapReq graphMapReq);

    void scheduleSwitch(Integer id, Integer status);

    void deleteSchedule(Integer id);

    void scheduleSwitchByKgName(GraphMapReq graphMapReq);

    void deleteDataByNotExistConcept(String kgName);

    List<JSONObject> listDatabase(String userId, String kgName);
}
