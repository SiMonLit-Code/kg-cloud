package com.plantdata.kgcloud.domain.dw.service;

import com.plantdata.kgcloud.domain.dw.entity.DWDatabase;
import com.plantdata.kgcloud.domain.dw.entity.DWTable;
import com.plantdata.kgcloud.domain.dw.req.DWTableCronReq;
import com.plantdata.kgcloud.domain.dw.req.RemoteTableAddReq;
import com.plantdata.kgcloud.domain.dw.rsp.DWDatabaseRsp;
import com.plantdata.kgcloud.domain.dw.rsp.DWTableRsp;
import com.plantdata.kgcloud.sdk.req.DWConnceReq;
import com.plantdata.kgcloud.sdk.req.DWDatabaseReq;
import com.plantdata.kgcloud.sdk.req.DWTableReq;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DWService {
    
    DWDatabase getDetail(Long databaseId);

    DWDatabaseRsp createDatabase(String userId, DWDatabaseReq req);

    void yamlUpload(String userId, Long databaseId, MultipartFile file);

    List<DWDatabaseRsp> findAll(String userId);

    DWDatabaseRsp setConn(String userId, DWConnceReq req);

    void upload(String userId, Long databaseId, Long tableId, MultipartFile file);

    DWTableRsp createTable(String userId, DWTableReq req);

    List<DataSetSchema> schemaResolve(MultipartFile file);

    List<DWTableRsp> findTableAll(String userId, Long databaseId);

    List<String> getRemoteTables(String userId, Long databaseId);

    List<Long> addRemoteTables(String userId, Long databaseId, List<RemoteTableAddReq> reqList);

    Object testConnect(DWConnceReq req);

    List<DWTable> getTableByIds(List<Long> tableIds);

    void setTableCron(String userId, DWTableCronReq req);
}
