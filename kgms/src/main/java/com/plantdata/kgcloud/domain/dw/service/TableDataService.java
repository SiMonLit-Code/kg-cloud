package com.plantdata.kgcloud.domain.dw.service;

import com.plantdata.kgcloud.domain.dw.req.DWFileTableBatchReq;
import com.plantdata.kgcloud.domain.dw.req.DWFileTableReq;
import com.plantdata.kgcloud.domain.dw.req.DWFileTableUpdateReq;
import com.plantdata.kgcloud.sdk.req.DwTableDataStatisticReq;
import com.plantdata.kgcloud.domain.dw.rsp.DWFileTableRsp;
import com.plantdata.kgcloud.sdk.req.DataOptQueryReq;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface TableDataService {
    Page<Map<String, Object>> getData(String userId, Long datasetId, Long tableId, DataOptQueryReq baseReq);

    Map<String, Object> getDataById(String userId, Long datasetId, Long tableId, String dataId);

    void fileAdd(DWFileTableReq req);

    Page<DWFileTableRsp> getFileData(String userId, Long databaseId, Long tableId, DataOptQueryReq baseReq);

    void fileUpdate(DWFileTableUpdateReq fileTableReq);

    void fileDelete(Integer id);

    void fileAddBatch(DWFileTableBatchReq fileTableReq, MultipartFile[] files);

    List<Map<String, Object>> statistic(String userId, Long datasetId, Long tableId, DwTableDataStatisticReq statisticReq);
}
