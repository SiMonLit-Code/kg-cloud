package com.plantdata.kgcloud.domain.dw.service;

import com.plantdata.kgcloud.sdk.req.DataOptQueryReq;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface TableDataService {
    Page<Map<String, Object>> getData(String userId, Long datasetId,Long tableId, DataOptQueryReq baseReq);

    Map<String, Object> getDataById(String userId, Long datasetId,Long tableId, String dataId);
}
