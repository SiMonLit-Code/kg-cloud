package ai.plantdata.kgcloud.domain.graph.config.service;


import ai.plantdata.kgcloud.sdk.rsp.GraphConfIndexStatusRsp;

public interface GraphConfIndexService {
    GraphConfIndexStatusRsp getStatus(String kgName);

    void updateStatus(String kgName, Integer i);
}
