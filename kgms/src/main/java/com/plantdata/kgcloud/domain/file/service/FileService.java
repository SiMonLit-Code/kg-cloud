package com.plantdata.kgcloud.domain.file.service;

import com.plantdata.kgcloud.domain.file.req.FileDatabaseNameReq;
import com.plantdata.kgcloud.domain.file.rsq.FileDatabaseRsp;
import com.plantdata.kgcloud.domain.file.rsq.FileTableRsp;

import java.util.List;

/**
 * @author lp
 * @date 2020/5/20 9:46
 */
public interface FileService {

    List<FileDatabaseRsp> findAll(String userId);

    List<FileDatabaseRsp> databaseTableList(String userId);

    List<FileTableRsp> findTableAll(String userId, Long databaseId);

    FileDatabaseRsp getDatabase(String userId, Long databaseId);

    FileDatabaseRsp createDatabase(String userId, String name);

    FileTableRsp createTable(String userId, Long databaseId, String name);

    void batchCreateTable(String userId, Long databaseId, List<String> names);

    void updateDatabaseName(String userId, FileDatabaseNameReq req);

    void deleteDatabase(String userId, Long databaseId);

    void deleteTable(String userId, Long databaseId, Long tableId);

    void deleteData(String userId, Long databaseId, Long tableId);

}