package com.plantdata.kgcloud.domain.file.service;

import com.plantdata.kgcloud.domain.file.rsq.FileSystemRsp;
import com.plantdata.kgcloud.domain.file.rsq.FolderRsp;

import java.util.List;

/**
 * @author lp
 * @date 2020/5/20 9:46
 */
public interface FileSystemService {

    List<FileSystemRsp> findFileSystem(String userId);

    List<FileSystemRsp> findAll(String userId);

    List<FolderRsp> findFolder(String userId, Long fileSystemId);

    FileSystemRsp get(String userId, Long fileSystemId);

    FileSystemRsp create(String userId, String name);

    FileSystemRsp createDefault(String userId);

    FolderRsp createFolder(String userId, Long fileSystemId, String name);

    void batchCreateFolder(String userId, Long fileSystemId, List<String> names);

    void updateName(String userId, Long fileSystemId, String name);

    void delete(String userId, Long fileSystemId);

    void deleteFolder(String userId, Long fileSystemId, Long folderId);

    void deleteData(String userId, Long fileSystemId, Long folderId);

}