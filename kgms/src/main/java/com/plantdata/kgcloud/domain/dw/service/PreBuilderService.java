package com.plantdata.kgcloud.domain.dw.service;

import com.plantdata.kgcloud.domain.dw.entity.DWDatabase;
import com.plantdata.kgcloud.domain.dw.rsp.PreBuilderConceptRsp;
import com.plantdata.kgcloud.domain.dw.rsp.PreBuilderMatchAttrRsp;
import com.plantdata.kgcloud.domain.dw.rsp.PreBuilderSearchRsp;
import com.plantdata.kgcloud.sdk.req.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PreBuilderService {
    Page<PreBuilderSearchRsp> findModel(String userId, PreBuilderSearchReq preBuilderSearchReq);

    List<PreBuilderMatchAttrRsp> matchAttr(String userId, PreBuilderMatchAttrReq preBuilderMatchAttrReq);

    Integer saveGraphMap(String userId, PreBuilderGraphMapReq preBuilderGraphMapReq);

    void createModelByYaml(DWDatabase database, List<PreBuilderConceptRsp> preBuilderConceptRspList);

    List<SchemaQuoteReq> getGraphMap(String userId, String kgName);

    PreBuilderSearchRsp databaseDetail(String userId, Long databaseId);
}
