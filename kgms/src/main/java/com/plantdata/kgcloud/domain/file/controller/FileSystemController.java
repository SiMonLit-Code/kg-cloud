package com.plantdata.kgcloud.domain.file.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.file.req.FileSystemNameReq;
import com.plantdata.kgcloud.domain.file.req.FileSystemReq;
import com.plantdata.kgcloud.domain.file.rsq.FileSystemRsp;
import com.plantdata.kgcloud.domain.file.rsq.FolderRsp;
import com.plantdata.kgcloud.domain.file.service.FileSystemService;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lp
 * @date 2020/5/20 9:44
 */
@Api(tags = "文件系统管理")
@RestController
@RequestMapping("/file/system")
public class FileSystemController {

    @Autowired
    private FileSystemService fileSystemService;

    @ApiOperation("文件系统管理-查找所有文件系统和文件夹")
    @GetMapping("/list/all")
    public ApiReturn<List<FileSystemRsp>> findAll() {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(fileSystemService.findAll(userId));
    }

    @ApiOperation("文件系统管理-查找所有文件系统")
    @GetMapping("/list")
    public ApiReturn<List<FileSystemRsp>> findFileSystem() {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(fileSystemService.findFileSystem(userId));
    }

    @ApiOperation("文件系统管理-查询文件夹")
    @GetMapping("list/folder/{fileSystemId}")
    public ApiReturn<List<FolderRsp>> findFolder(@PathVariable("fileSystemId") Long fileSystemId) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(fileSystemService.findFolder(userId, fileSystemId));
    }

    @ApiOperation("文件系统管理-查找文件系统详情")
    @GetMapping("/get/{fileSystemId}")
    public ApiReturn<FileSystemRsp> get(@PathVariable("fileSystemId") Long fileSystemId) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(fileSystemService.get(userId, fileSystemId));
    }

    @ApiOperation("文件系统管理-创建文件系统")
    @PostMapping("/create")
    public ApiReturn<FileSystemRsp> create(@RequestBody FileSystemNameReq req) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(fileSystemService.create(userId, req.getName()));
    }

    @ApiOperation("文件系统管理-创建文件夹")
    @PostMapping("/create/folder/{fileSystemId}")
    public ApiReturn<FolderRsp> createFolder(@PathVariable("fileSystemId") Long fileSystemId,
                                             @RequestBody FileSystemNameReq req) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(fileSystemService.createFolder(userId, fileSystemId, req.getName()));
    }

    @ApiOperation("文件系统管理-批量创建文件夹")
    @PostMapping("/batch/create/folder/{fileSystemId}")
    public ApiReturn batchCreateFolder(@PathVariable("fileSystemId") Long fileSystemId,
                                       @RequestBody List<String> names) {
        String userId = SessionHolder.getUserId();
        fileSystemService.batchCreateFolder(userId, fileSystemId, names);
        return ApiReturn.success();
    }

    @ApiOperation("文件系统管理-更新文件系统名")
    @PostMapping("/update/name")
    public ApiReturn updateName(@RequestBody FileSystemReq req) {
        String userId = SessionHolder.getUserId();
        fileSystemService.updateName(userId, req.getFileSystemId(), req.getName());
        return ApiReturn.success();
    }

    @ApiOperation("文件系统管理-删除文件系统")
    @DeleteMapping("/delete/{fileSystemId}")
    public ApiReturn delete(@PathVariable("fileSystemId") Long fileSystemId) {
        String userId = SessionHolder.getUserId();
        fileSystemService.delete(userId, fileSystemId);
        return ApiReturn.success();
    }

    @ApiOperation("文件系统管理-删除文件夹")
    @DeleteMapping("/delete/folder/{fileSystemId}/{folderId}")
    public ApiReturn deleteFolder(@PathVariable("fileSystemId") Long fileSystemId,
                                  @PathVariable("folderId") Long folderId) {
        String userId = SessionHolder.getUserId();
        fileSystemService.deleteFolder(userId, fileSystemId, folderId);
        return ApiReturn.success();
    }

    @ApiOperation("文件系统管理-清空文件夹数据")
    @DeleteMapping("/delete/data/{fileSystemId}/{folderId}")
    public ApiReturn deleteData(@PathVariable("fileSystemId") Long fileSystemId,
                                @PathVariable("folderId") Long folderId) {
        String userId = SessionHolder.getUserId();
        fileSystemService.deleteData(userId, fileSystemId, folderId);
        return ApiReturn.success();
    }
}
