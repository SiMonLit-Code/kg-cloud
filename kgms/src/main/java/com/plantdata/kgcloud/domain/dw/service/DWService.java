package com.plantdata.kgcloud.domain.dw.service;

import com.alibaba.fastjson.JSONObject;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.domain.dw.entity.DWTable;
import com.plantdata.kgcloud.domain.dw.req.*;
import com.plantdata.kgcloud.domain.dw.rsp.*;
import com.plantdata.kgcloud.sdk.req.DWConnceReq;
import com.plantdata.kgcloud.sdk.req.DWDatabaseReq;
import com.plantdata.kgcloud.sdk.req.DWTableReq;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.sdk.rsp.*;
import com.plantdata.kgcloud.domain.dw.rsp.PreBuilderConceptRsp;
import com.plantdata.kgcloud.sdk.req.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DWService {

    DWDatabaseRsp getDetail(Long databaseId);

    DWDatabaseRsp createDatabase(String userId, DWDatabaseReq req);

    void yamlUpload(Long databaseId, MultipartFile file);

    List<DWDatabaseRsp> findAll(String userId);

    DWDatabaseRsp setConn(String userId, DWConnceReq req);

    void upload(String userId, Long databaseId, Long tableId, MultipartFile file);

    DWTableRsp createTable(String userId, DWTableReq req);

    List<DataSetSchema> schemaResolve(MultipartFile file, Integer dataForamt);

    List<DWTableRsp> findTableAll(String userId, Long databaseId);

    List<JSONObject> getRemoteTables(String userId, Long databaseId);

    void addRemoteTables(String userId, Long databaseId, List<RemoteTableAddReq> reqList);

    Object testConnect(DWConnceReq req);

    List<DWTable> getTableByIds(List<Long> tableIds);

    List<PreBuilderConceptRsp> modelSchema2PreBuilder(List<ModelSchemaConfigRsp> modelSchemaConfig);

    void setTableCron(String userId, List<DWTableCronReq> req);

    Page<DWDatabaseRsp> list(String userId, DWDatabaseQueryReq req);

    void tagUpload(Long databaseId, MultipartFile file);

    Integer push(String userId, ModelPushReq req);

    void setTableScheduling(String userId, DWTableSchedulingReq req);

    ModelSchemaConfigRsp getModel(String userId, Long id);

    DWTableRsp findTableByTableName(String userId, Long databaseId, String tableName);

    void unifiedScheduling(String userId, DWTableCronReq req);

    void modelUpload(Long databaseId, MultipartFile file);

    List<DWDatabaseRsp> databaseTableList(String userId);

    List<JSONObject> getDatabaseMappingTable(String userId, Long databaseId);

    DWDatabaseRsp getDatabase(String userId, Long id);

    void deteleDatabase(String userId, Long id);

    void deleteTable(String userId, Long databaseId, Long tableId);

    DWTable getTableDetail(Long tableId);

    void batchCreateTable(String userId, List<DWTableReq> reqs);

    void updateDatabaseName(String userId, DWDatabaseNameReq req);

    void exampleDownload(String userId, Long databaseId, HttpServletResponse response);

    void deleteData(String userId, Long databaseId, Long tableId);

    DWDatabaseRsp getDbByDataName(String dataName);

    BasePage<TableLogListRsp> tableLogList(DWTableLogReq dwTableLogReq);

    List<DWDataStatusRsp> dataStatusList(Long databaseId);
    void updateTagJson(Long databaseId, List<TagJsonReq> tagJsonReqs);

    List<ModelSchemaConfigRsp> getTagJson(Long databaseId);

    List<CustomTableRsp> getCustomLabel(Long databaseId, Boolean isDefault);

    List<String> getTableFieldEnum(Long databaseId, String tableName, String field);

    void updateCustomLabel(Long databaseId, List<CustomTableRsp> customTableRsps);

    DWDatabaseRsp findDatabaseByDataName(String dataName);

<<<<<<< HEAD
=======
    DWDatabaseRsp findById(String userId,Integer dbId);

    DW2dTableRsp statisticBy2DTable(SqlQueryReq req);

    DW3dTableRsp statisticBy3DTable(SqlQueryReq req);
>>>>>>> e09807e3af3406b9d4e775e329aab496dabe439e
}
