package com.plantdata.kgcloud.domain.file.service.impl;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.plantdata.kgcloud.constant.FileConstants;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.file.entity.FileDatabase;
import com.plantdata.kgcloud.domain.file.entity.FileTable;
import com.plantdata.kgcloud.domain.file.repository.FileDatabaseRepository;
import com.plantdata.kgcloud.domain.file.repository.FileTableRepository;
import com.plantdata.kgcloud.domain.file.req.FileDatabaseNameReq;
import com.plantdata.kgcloud.domain.file.rsq.FileDatabaseRsp;
import com.plantdata.kgcloud.domain.file.rsq.FileTableRsp;
import com.plantdata.kgcloud.domain.file.service.FileService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lp
 * @date 2020/5/20 9:47
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileDatabaseRepository databaseRepository;
    @Autowired
    private FileTableRepository tableRepository;
    @Autowired
    private MongoClient mongoClient;

    private MongoCollection<Document> getFileCollection() {
        return mongoClient.getDatabase(FileConstants.DW_PREFIX + SessionHolder.getUserId()).getCollection(FileConstants.FILE);
    }

    private final Function<FileDatabase, FileDatabaseRsp> database2rsp = (s) -> {
        FileDatabaseRsp databaseRsp = new FileDatabaseRsp();
        BeanUtils.copyProperties(s, databaseRsp);
        return databaseRsp;
    };

    private final Function<FileTable, FileTableRsp> table2rsp = (s) -> {
        FileTableRsp tableRsp = new FileTableRsp();
        BeanUtils.copyProperties(s, tableRsp);
        return tableRsp;
    };

    @Override
    public List<FileDatabaseRsp> findAll(String userId) {
        FileDatabase database = FileDatabase.builder()
                .userId(SessionHolder.getUserId())
                .build();
        List<FileDatabase> all = databaseRepository.findAll(Example.of(database), Sort.by(Sort.Order.desc("createAt")));
        return all.stream().map(database2rsp).collect(Collectors.toList());
    }

    @Override
    public List<FileDatabaseRsp> databaseTableList(String userId) {
        List<FileDatabaseRsp> databases = findAll(userId);
        if (databases == null || databases.isEmpty()) {
            return new ArrayList<>();
        }

        for (FileDatabaseRsp databaseRsp : databases) {
            List<FileTableRsp> tables = findTableAll(userId, databaseRsp.getId());
            // 文件系统，添加文件夹拥有文件数量参数
            for (FileTableRsp tableRsp : tables) {
                tableRsp.setFileCount(setTableFileCount(databaseRsp.getId(), tableRsp.getId()));
            }
            databaseRsp.setTables(tables);
        }
        return databases;
    }

    @Override
    public List<FileTableRsp> findTableAll(String userId, Long databaseId) {
        FileTable table = FileTable.builder()
                .fileDataBaseId(databaseId)
                .build();
        List<FileTable> dwTableList = tableRepository.findAll(Example.of(table), Sort.by(Sort.Order.desc("createAt")));

        List<FileTableRsp> tables = dwTableList.stream().map(table2rsp).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tables)) {
            return tables;
        }

        // 文件系统，添加文件夹拥有文件数量参数
        for (FileTableRsp tableRsp : tables) {
            tableRsp.setFileCount(setTableFileCount(databaseId, tableRsp.getId()));
        }

        return tables;
    }

    @Override
    public FileDatabaseRsp getDatabase(String userId, Long databaseId) {
        FileDatabase database = FileDatabase.builder()
                .id(databaseId).userId(userId)
                .build();
        Optional<FileDatabase> optional = databaseRepository.findOne(Example.of(database));

        if (!optional.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.FILE_DATABASE_NOT_EXIST);
        }

        return ConvertUtils.convert(FileDatabaseRsp.class).apply(optional.get());
    }

    @Override
    public FileDatabaseRsp createDatabase(String userId, String name) {
        FileDatabase database = FileDatabase.builder()
                .userId(SessionHolder.getUserId())
                .title(name)
                .build();
        FileDatabase save = databaseRepository.save(database);

        return database2rsp.apply(save);
    }

    @Override
    public FileTableRsp createTable(String userId, Long databaseId, String name) {
        FileDatabaseRsp dwDatabase = getDatabase(userId, databaseId);

        if (dwDatabase == null) {
            throw BizException.of(KgmsErrorCodeEnum.FILE_DATABASE_NOT_EXIST);
        }

        // 验证数据表名称是否存在
        Optional<FileTable> optional = tableRepository.findOne(Example.of(FileTable.builder().fileDataBaseId(databaseId).title(name).build()));

        if (optional.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.FILE_TABLE_NAME_EXIST);
        }

        FileTable table = FileTable.builder()
                .title(name).fileDataBaseId(databaseId)
                .build();
        FileTable save = tableRepository.save(table);

        return table2rsp.apply(save);
    }

    @Override
    public void batchCreateTable(String userId, Long databaseId, List<String> names) {
        if (CollectionUtils.isEmpty(names)) {
            return;
        }

        for (String name : names) {
            createTable(userId, databaseId, name);
        }
    }

    @Override
    public void updateDatabaseName(String userId, FileDatabaseNameReq req) {
        Optional<FileDatabase> optional = databaseRepository.findById(req.getDatabaseId());

        if (!optional.isPresent()) {
            return;
        }

        FileDatabase database = optional.get();

        if (Objects.equals(database.getUserId(), userId)) {
            database.setTitle(req.getName());
            databaseRepository.save(database);
        }
    }

    @Override
    public void deleteDatabase(String userId, Long id) {
        FileDatabase database = FileDatabase.builder()
                .userId(userId).id(id)
                .build();
        Optional<FileDatabase> optional = databaseRepository.findOne(Example.of(database));

        if (!optional.isPresent()) {
            return;
        }

        // 删除mongo文件数据
        getFileCollection().deleteMany(Filters.eq("databaseId", id));

        List<FileTableRsp> tables = findTableAll(userId, id);
        if (!CollectionUtils.isEmpty(tables)) {
            for (FileTableRsp tableRsp : tables) {
                deleteTable(userId, id, tableRsp.getId());
            }
        }

        databaseRepository.deleteById(id);
    }

    @Override
    public void deleteTable(String userId, Long databaseId, Long tableId) {
        FileDatabase database = FileDatabase.builder()
                .id(databaseId).userId(userId)
                .build();
        Optional<FileDatabase> optional = databaseRepository.findOne(Example.of(database));

        if (!optional.isPresent()) {
            return;
        }

        FileTable table = FileTable.builder()
                .id(tableId).fileDataBaseId(databaseId)
                .build();
        Optional<FileTable> tableOptional = tableRepository.findOne(Example.of(table));

        if (!tableOptional.isPresent()) {
            return;
        }

        // 删除mongo文件数据
        getFileCollection().deleteMany(Filters.eq("tableId", tableId));

        tableRepository.deleteById(tableId);
    }

    @Override
    public void deleteData(String userId, Long databaseId, Long tableId) {
        FileDatabase database = FileDatabase.builder()
                .id(databaseId).userId(userId)
                .build();
        Optional<FileDatabase> optional = databaseRepository.findOne(Example.of(database));

        if (!optional.isPresent()) {
            return;
        }

        getFileCollection().deleteMany(Filters.eq("tableId", tableId));
    }

    private Long setTableFileCount(Long databaseId, Long tableId) {
        List<Bson> query = new ArrayList<>(2);
        query.add(Filters.eq("databaseId", databaseId));
        query.add(Filters.eq("tableId", tableId));
        return getFileCollection().countDocuments(Filters.and(query));
    }

}
