package com.plantdata.kgcloud.domain.dw.service;

import com.alibaba.fastjson.JSONObject;
import com.plantdata.kgcloud.domain.dw.req.ModelPushReq;
import com.plantdata.kgcloud.domain.dw.req.PreBuilderCountReq;
import com.plantdata.kgcloud.domain.dw.req.PreBuilderCreateReq;
import com.plantdata.kgcloud.domain.dw.req.PreBuilderUpdateReq;
import com.plantdata.kgcloud.domain.dw.rsp.*;
import com.plantdata.kgcloud.sdk.req.PreBuilderGraphMapReq;
import com.plantdata.kgcloud.sdk.req.PreBuilderMatchAttrReq;
import com.plantdata.kgcloud.sdk.req.PreBuilderSearchReq;
import com.plantdata.kgcloud.sdk.req.SchemaQuoteReq;
import com.plantdata.kgcloud.sdk.rsp.CustomTableRsp;
import com.plantdata.kgcloud.sdk.rsp.DWDatabaseRsp;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PreBuilderService {
    Page<PreBuilderSearchRsp> findModel(String userId, PreBuilderSearchReq preBuilderSearchReq);

    Page<PreBuilderMatchAttrRsp> matchAttr(String userId, PreBuilderMatchAttrReq preBuilderMatchAttrReq);

    JSONObject saveGraphMap(String userId, PreBuilderGraphMapReq preBuilderGraphMapReq);

    List<SchemaQuoteReq> getGraphMap(String userId, String kgName,boolean isDelete);

    PreBuilderSearchRsp databaseDetail(String userId, Long databaseId);

    Page<PreBuilderSearchRsp> listManage(String userId, PreBuilderSearchReq preBuilderSearchReq);

    void delete(String userId, Integer id);

    void update(String userId, Integer id, String status);

    Integer createModel(DWDatabaseRsp database, List<PreBuilderConceptRsp> preBuilderConceptRspList, String modelType, List<CustomTableRsp> labels);

    List<String> getTypes(String userId,Boolean isManage);

    String create(PreBuilderCreateReq req);

    void createSchedulingConfig(String kgName,boolean isCreateKtr,Integer status);

    void pushGraphModel(String userId, ModelPushReq req);

    void updateModel(PreBuilderUpdateReq req);

    void updateStatusByDatabaseId(Long databaseId, int status);

    PreBuilderCountReq matchAttrCount(String userId, PreBuilderMatchAttrReq preBuilderMatchAttrReq);
}
