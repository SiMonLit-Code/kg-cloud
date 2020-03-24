package com.plantdata.kgcloud.domain.dw.service;

import com.plantdata.kgcloud.domain.dw.entity.DWDatabase;
import com.plantdata.kgcloud.domain.dw.entity.DWTable;
import com.plantdata.kgcloud.domain.dw.req.DWDatabaseQueryReq;
import com.plantdata.kgcloud.domain.dw.req.DWTableCronReq;
import com.plantdata.kgcloud.domain.dw.req.DWTableSchedulingReq;
import com.plantdata.kgcloud.domain.dw.req.RemoteTableAddReq;
import com.plantdata.kgcloud.domain.dw.rsp.DWDatabaseRsp;
import com.plantdata.kgcloud.domain.dw.rsp.DWTableRsp;
import com.plantdata.kgcloud.domain.dw.rsp.ModelSchemaConfigRsp;
import com.plantdata.kgcloud.domain.edit.rsp.FilePathRsp;
import com.plantdata.kgcloud.sdk.req.DWConnceReq;
import com.plantdata.kgcloud.sdk.req.DWDatabaseReq;
import com.plantdata.kgcloud.sdk.req.DWTableReq;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DWService {
    
    DWDatabase getDetail(Long databaseId);

    DWDatabaseRsp createDatabase(String userId, DWDatabaseReq req);

    void yamlUpload(Long databaseId, MultipartFile file);

    List<DWDatabaseRsp> findAll(String userId);

    DWDatabaseRsp setConn(String userId, DWConnceReq req);

    void upload(String userId, Long databaseId, Long tableId, MultipartFile file);

    DWTableRsp createTable(String userId, DWTableReq req);

    List<DataSetSchema> schemaResolve(MultipartFile file,Integer dataForamt);

    List<DWTableRsp> findTableAll(String userId, Long databaseId);

    List<String> getRemoteTables(String userId, Long databaseId);

    void addRemoteTables(String userId, Long databaseId, List<RemoteTableAddReq> reqList);

    Object testConnect(DWConnceReq req);

    List<DWTable> getTableByIds(List<Long> tableIds);

    void setTableCron(String userId, List<DWTableCronReq> req);

    Page<DWDatabaseRsp> list(String userId, DWDatabaseQueryReq req);

    void tagUpload(Long databaseId, MultipartFile file);

    void push(String userId, Long id,String modelType);

    void setTableScheduling(String userId, DWTableSchedulingReq req);

    ModelSchemaConfigRsp getModel(String userId, Long id);

    DWTableRsp findTableByTableName(String userId, Long databaseId, String tableName);
}
