package com.plantdata.kgcloud.domain.dw.service;

import com.plantdata.kgcloud.domain.dw.entity.DWDatabase;
import com.plantdata.kgcloud.domain.dw.rsp.PreBuilderConceptRsp;
import com.plantdata.kgcloud.domain.dw.rsp.PreBuilderMatchAttrRsp;
import com.plantdata.kgcloud.domain.dw.rsp.PreBuilderSearchRsp;
import com.plantdata.kgcloud.sdk.req.PreBuilderGraphMapReq;
import com.plantdata.kgcloud.sdk.req.PreBuilderMatchAttrReq;
import com.plantdata.kgcloud.sdk.req.PreBuilderSearchReq;
import com.plantdata.kgcloud.sdk.req.SchemaQuoteReq;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PreBuilderService {
    Page<PreBuilderSearchRsp> findModel(String userId, PreBuilderSearchReq preBuilderSearchReq);

    List<PreBuilderMatchAttrRsp> matchAttr(String userId, PreBuilderMatchAttrReq preBuilderMatchAttrReq);

    void saveGraphMap(String userId, PreBuilderGraphMapReq preBuilderGraphMapReq);

    List<SchemaQuoteReq> getGraphMap(String userId, String kgName);

    PreBuilderSearchRsp databaseDetail(String userId, Long databaseId);

    Page<PreBuilderSearchRsp> listManage(String userId, PreBuilderSearchReq preBuilderSearchReq);

    void delete(String userId, Integer id);

    void update(String userId, Integer id, String status);

    void createModel(DWDatabase database, List<PreBuilderConceptRsp> preBuilderConceptRspList, String modelType,String yamlContent);

    List<String> getTypes(String userId);

}
