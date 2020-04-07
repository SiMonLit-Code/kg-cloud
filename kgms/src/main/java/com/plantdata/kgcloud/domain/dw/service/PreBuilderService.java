package com.plantdata.kgcloud.domain.dw.service;

import com.alibaba.fastjson.JSONObject;
import com.plantdata.kgcloud.domain.dw.req.ModelPushReq;
import com.plantdata.kgcloud.domain.dw.req.PreBuilderCreateReq;
import com.plantdata.kgcloud.domain.dw.req.PreBuilderUpdateReq;
import com.plantdata.kgcloud.domain.dw.rsp.DWDatabaseRsp;
import com.plantdata.kgcloud.domain.dw.rsp.PreBuilderConceptRsp;
import com.plantdata.kgcloud.domain.dw.rsp.PreBuilderMatchAttrRsp;
import com.plantdata.kgcloud.domain.dw.rsp.PreBuilderSearchRsp;
import com.plantdata.kgcloud.sdk.req.PreBuilderGraphMapReq;
import com.plantdata.kgcloud.sdk.req.PreBuilderMatchAttrReq;
import com.plantdata.kgcloud.sdk.req.PreBuilderSearchReq;
import com.plantdata.kgcloud.sdk.req.SchemaQuoteReq;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PreBuilderService {
    Page<PreBuilderSearchRsp> findModel(String userId, PreBuilderSearchReq preBuilderSearchReq);

    List<PreBuilderMatchAttrRsp> matchAttr(String userId, PreBuilderMatchAttrReq preBuilderMatchAttrReq);

    JSONObject saveGraphMap(String userId, PreBuilderGraphMapReq preBuilderGraphMapReq);

    List<SchemaQuoteReq> getGraphMap(String userId, String kgName);

    PreBuilderSearchRsp databaseDetail(String userId, Long databaseId);

    Page<PreBuilderSearchRsp> listManage(String userId, PreBuilderSearchReq preBuilderSearchReq);

    void delete(String userId, Integer id);

    void update(String userId, Integer id, String status);

    void createModel(DWDatabaseRsp database, List<PreBuilderConceptRsp> preBuilderConceptRspList, String modelType, String yamlContent);

    List<String> getTypes(String userId,Boolean isManage);

    void create(PreBuilderCreateReq req);

    void createSchedulingConfig(String kgName,boolean isCreateKtr,Integer status);

    void pushGraphModel(String userId, ModelPushReq req);

    void updateModel(PreBuilderUpdateReq req);
}
