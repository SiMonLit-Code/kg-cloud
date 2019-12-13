package com.plantdata.kgcloud.domain.graph.clash.service;

import com.plantdata.kgcloud.domain.graph.clash.entity.ClashListReq;
import com.plantdata.kgcloud.domain.graph.clash.entity.ClashToGraphReq;

import java.util.List;
import java.util.Map;

public interface ClashService {

    void toGraph(String kgName, ClashToGraphReq request);

    Map<String, Object> list(String kgName, ClashListReq req);

    void delete(String kgName, List<String> ls);
}
