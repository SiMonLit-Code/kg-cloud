package com.plantdata.kgcloud.domain.file.service.impl;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.plantdata.kgcloud.constant.FileConstants;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.file.entity.FileFolder;
import com.plantdata.kgcloud.domain.file.entity.FileSystem;
import com.plantdata.kgcloud.domain.file.repository.FileFolderRepository;
import com.plantdata.kgcloud.domain.file.repository.FileSystemRepository;
import com.plantdata.kgcloud.domain.file.rsq.FileSystemRsp;
import com.plantdata.kgcloud.domain.file.rsq.FolderRsp;
import com.plantdata.kgcloud.domain.file.service.FileDataService;
import com.plantdata.kgcloud.domain.file.service.FileSystemService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class FileSystemServiceImpl implements FileSystemService {

    @Autowired
    private FileSystemRepository fileSystemRepository;
    @Autowired
    private FileFolderRepository fileFolderRepository;
    @Autowired
    private MongoClient mongoClient;
    @Autowired
    private FileDataService fileDataService;


    @Value("${mongo.prefix:}")
    private String mongoPrefix;

    private MongoCollection<Document> getFileCollection() {
        return mongoClient.getDatabase(mongoPrefix+FileConstants.ENTITY_FILE_PREFIX + SessionHolder.getUserId()).getCollection(FileConstants.FILE);
    }

    private final Function<FileSystem, FileSystemRsp> fileSystem2rsp = (s) -> {
        FileSystemRsp fileSystemRsp = new FileSystemRsp();
        BeanUtils.copyProperties(s, fileSystemRsp);
        return fileSystemRsp;
    };

    private final Function<FileFolder, FolderRsp> table2rsp = (s) -> {
        FolderRsp tableRsp = new FolderRsp();
        BeanUtils.copyProperties(s, tableRsp);
        return tableRsp;
    };

    @Override
    public List<FileSystemRsp> findFileSystem(String userId) {
        FileSystem fileSystem = FileSystem.builder()
                .userId(SessionHolder.getUserId()).isDeleted(false)
                .build();
        List<FileSystem> all = fileSystemRepository.findAll(Example.of(fileSystem), Sort.by(Sort.Order.desc("createAt")));
        return all.stream().map(fileSystem2rsp).collect(Collectors.toList());
    }

    @Override
    public List<FileSystemRsp> findAll(String userId) {
        List<FileSystemRsp> fileSystems = findFileSystem(userId);
        if (fileSystems == null || fileSystems.isEmpty()) {
            return new ArrayList<>();
        }

        // 默认文件系统
        FileSystemRsp fileSystem = null;
        for (int i = 0; i < fileSystems.size(); i++) {
            FileSystemRsp fileSystemRsp = fileSystems.get(i);
            List<FolderRsp> tables = findFolder(userId, fileSystemRsp.getId());
            fileSystemRsp.setFileFolders(tables);
            if (fileSystemRsp.getIsDefault()) {
                fileSystem = fileSystemRsp;
                fileSystems.remove(i);
                i--;
            }
        }
        List<FileSystemRsp> newFileSystems = Lists.newArrayList();
        if (fileSystem != null) {
            newFileSystems.add(fileSystem);
        }
        newFileSystems.addAll(fileSystems);
        return newFileSystems;
    }

    @Override
    public List<FolderRsp> findFolder(String userId, Long fileSystemId) {
        FileFolder table = FileFolder.builder()
                .fileSystemId(fileSystemId).isDeleted(false)
                .build();
        List<FileFolder> fileFolders = fileFolderRepository.findAll(Example.of(table), Sort.by(Sort.Order.desc("createAt")));

        List<FolderRsp> folderRsps = fileFolders.stream().map(table2rsp).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(folderRsps)) {
            return folderRsps;
        }

        // 默认文件夹
        FolderRsp folder = null;
        // 文件系统，添加文件夹拥有文件数量参数
        for (int i = 0; i < folderRsps.size(); i++) {
            FolderRsp folderRsp = folderRsps.get(i);
            if (folderRsp.getIsDefault()) {
                folder = folderRsp;
                folderRsps.remove(i);
                i--;
            }
            folderRsp.setFileCount(setTableFileCount(fileSystemId, folderRsp.getId()));
        }

        List<FolderRsp> newFolderRsps = Lists.newArrayList();
        if (folder != null) {
            newFolderRsps.add(folder);
        }
        newFolderRsps.addAll(folderRsps);
        return newFolderRsps;
    }

    @Override
    public FileSystemRsp get(String userId, Long fileSystemId) {
        FileSystem fileSystem = FileSystem.builder()
                .id(fileSystemId).userId(userId).isDeleted(false)
                .build();
        Optional<FileSystem> optional = fileSystemRepository.findOne(Example.of(fileSystem));

        if (!optional.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.FILE_DATABASE_NOT_EXIST);
        }

        return ConvertUtils.convert(FileSystemRsp.class).apply(optional.get());
    }

    @Override
    public FileSystemRsp create(String userId, String name) {
        FileSystem fileSystem = FileSystem.builder()
                .userId(SessionHolder.getUserId())
                .name(name).isDeleted(false)
                .build();

        // 验证文件系统名称是否存在
        Optional<FileSystem> optional = fileSystemRepository.findOne(Example.of(fileSystem));
        if (optional.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.FILE_DATABASE_NAME_EXIST);
        }

        fileSystem.setIsDefault(false);
        FileSystem save = fileSystemRepository.save(fileSystem);

        return fileSystem2rsp.apply(save);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileSystemRsp createDefault(String userId) {
        FileSystem fileSystem = FileSystem.builder()
                .userId(userId).isDefault(true)
                .build();
        Optional<FileSystem> optional = fileSystemRepository.findOne(Example.of(fileSystem));
        if (optional.isPresent()) {
            FileSystem oldFileSystem = optional.get();
            FileFolder fileFolder = FileFolder.builder()
                    .fileSystemId(oldFileSystem.getId()).isDefault(true)
                    .build();
            Optional<FileFolder> fileOptional = fileFolderRepository.findOne(Example.of(fileFolder));
            if (!fileOptional.isPresent()) {
                fileFolder.setIsDeleted(false);
                fileFolder.setName("默认文件夹");
                fileFolderRepository.save(fileFolder);
            }
            return ConvertUtils.convert(FileSystemRsp.class).apply(oldFileSystem);
        } else {
            fileSystem.setIsDeleted(false);
            fileSystem.setName("默认文件系统");
            FileSystem newFileSystem = fileSystemRepository.save(fileSystem);
            FileFolder fileFolder = FileFolder.builder()
                    .fileSystemId(newFileSystem.getId()).name("默认文件夹")
                    .isDefault(true).isDeleted(false)
                    .build();
            fileFolderRepository.save(fileFolder);
            return ConvertUtils.convert(FileSystemRsp.class).apply(newFileSystem);
        }
    }

    @Override
    public FolderRsp createFolder(String userId, Long fileSystemId, String name) {
        FileSystemRsp fileSystemRsp = get(userId, fileSystemId);

        if (fileSystemRsp == null) {
            throw BizException.of(KgmsErrorCodeEnum.FILE_DATABASE_NOT_EXIST);
        }

        FileFolder fileFolder = FileFolder.builder()
                .name(name).fileSystemId(fileSystemId).isDeleted(false)
                .build();

        // 验证文件夹名称是否存在
        Optional<FileFolder> optional = fileFolderRepository.findOne(Example.of(fileFolder));

        if (optional.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.FILE_TABLE_NAME_EXIST);
        }

        fileFolder.setIsDefault(false);
        FileFolder save = fileFolderRepository.save(fileFolder);

        return table2rsp.apply(save);
    }

    @Override
    public void batchCreateFolder(String userId, Long fileSystemId, List<String> names) {
        if (CollectionUtils.isEmpty(names)) {
            return;
        }

        for (String name : names) {
            createFolder(userId, fileSystemId, name);
        }
    }

    @Override
    public void updateName(String userId, Long fileSystemId, String name) {
        Optional<FileSystem> optional = fileSystemRepository.findById(fileSystemId);

        if (!optional.isPresent()) {
            return;
        }

        FileSystem fileSystem = optional.get();

        if (Objects.equals(fileSystem.getUserId(), userId)) {
            fileSystem.setName(name);
            fileSystemRepository.save(fileSystem);
        }
    }

    @Override
    public void delete(String userId, Long fileSystemId) {
        FileSystem fileSystem = FileSystem.builder()
                .userId(userId).id(fileSystemId)
                .build();
        Optional<FileSystem> optional = fileSystemRepository.findOne(Example.of(fileSystem));

        if (!optional.isPresent()) {
            return;
        }

        List<FolderRsp> tables = findFolder(userId, fileSystemId);
        if (!CollectionUtils.isEmpty(tables)) {
            for (FolderRsp tableRsp : tables) {
                deleteFolder(userId, fileSystemId, tableRsp.getId());
            }
        }

        FileSystem newFileSystem = optional.get();
        newFileSystem.setIsDeleted(true);
        fileSystemRepository.save(newFileSystem);
    }

    @Override
    public void deleteFolder(String userId, Long fileSystemId, Long folderId) {
        FileSystem fileSystem = FileSystem.builder()
                .id(fileSystemId).userId(userId)
                .build();
        Optional<FileSystem> optional = fileSystemRepository.findOne(Example.of(fileSystem));

        if (!optional.isPresent()) {
            return;
        }

        FileFolder fileFolder = FileFolder.builder()
                .id(folderId).fileSystemId(fileSystemId)
                .build();
        Optional<FileFolder> folderOptional = fileFolderRepository.findOne(Example.of(fileFolder));

        if (!folderOptional.isPresent()) {
            return;
        }

        // 删除mongo文件数据
        fileDataService.fileDeleteByFolderId(folderId);

        FileFolder newFileFolder = folderOptional.get();
        newFileFolder.setIsDeleted(true);
        fileFolderRepository.save(newFileFolder);
    }

    @Override
    public void deleteData(String userId, Long fileSystemId, Long folderId) {
        FileSystem fileSystem = FileSystem.builder()
                .id(fileSystemId).userId(userId)
                .build();
        Optional<FileSystem> optional = fileSystemRepository.findOne(Example.of(fileSystem));

        if (!optional.isPresent()) {
            return;
        }

        // 删除mongo文件数据
        fileDataService.fileDeleteByFolderId(folderId);
    }

    private Long setTableFileCount(Long fileSystemId, Long folderId) {
        List<Bson> query = new ArrayList<>(2);
        query.add(Filters.eq("fileSystemId", fileSystemId));
        query.add(Filters.eq("folderId", folderId));
        return getFileCollection().countDocuments(Filters.and(query));
    }

}
