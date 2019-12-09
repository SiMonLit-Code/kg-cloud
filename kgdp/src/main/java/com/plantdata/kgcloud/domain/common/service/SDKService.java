package com.plantdata.kgcloud.domain.common.service;

import com.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.BatchRelationRsp;

import java.util.List;

public interface SDKService {
    Long getEntityIdByName(String kgName, String name, Long conceptId);

    InfoBoxRsp infobox(String kgName, Long entityId);

    List<BatchRelationRsp> addBatchRelation(String kgName, List<BatchRelationRsp> relationList);

    List<OpenBatchSaveEntityRsp> addBatchEntity(String kgName, List<OpenBatchSaveEntityRsp> entityList);

}
