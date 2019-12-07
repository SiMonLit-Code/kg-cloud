package com.plantdata.kgcloud.domain.dataset.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.dataset.service.DataSetFolderService;
import com.plantdata.kgcloud.domain.dataset.service.DataSetService;
import com.plantdata.kgcloud.sdk.req.DataSetPageReq;
import com.plantdata.kgcloud.sdk.req.DataSetReq;
import com.plantdata.kgcloud.sdk.req.FolderReq;
import com.plantdata.kgcloud.sdk.rsp.DataSetRsp;
import com.plantdata.kgcloud.sdk.rsp.FolderRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-06 10:27
 **/
@Api(tags = "数据集管理")
@RestController
@RequestMapping("/dataset")
public class DataSetController {
    @Autowired
    private DataSetService dataSetService;

    @Autowired
    private DataSetFolderService dataSetFolderService;

    @ApiOperation("数据集-schema-查找所有")
    @GetMapping("/all")
    public ApiReturn<List<DataSetRsp>> findAll() {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dataSetService.findAll(userId));
    }

    @ApiOperation("数据集-schema-分页查找")
    @GetMapping("/")
    public ApiReturn<Page<DataSetRsp>> findAll(DataSetPageReq req) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dataSetService.findAll(userId, req));
    }

    @ApiOperation("数据集-schema-根据Id查找")
    @GetMapping("/{id}")
    public ApiReturn<DataSetRsp> findById(@PathVariable Long id) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dataSetService.findById(userId, id));
    }

    @ApiOperation("数据集-schema-创建")
    @PostMapping("/")
    public ApiReturn<DataSetRsp> insert(@Valid @RequestBody DataSetReq req) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dataSetService.insert(userId,req));
    }

    @ApiOperation("数据集-schema-编辑")
    @PatchMapping("/{id}")
    public ApiReturn<DataSetRsp> update(@PathVariable Long id, @Valid @RequestBody DataSetReq req) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dataSetService.update(userId, id, req));
    }

    @ApiOperation("数据集-schema-删除")
    @DeleteMapping("/{id}")
    public ApiReturn delete(@PathVariable Long id) {
        String userId = SessionHolder.getUserId();
        dataSetService.delete(userId, id);
        return ApiReturn.success();
    }


    @ApiOperation("数据集-文件夹-列表")
    @GetMapping("/folder")
    public ApiReturn<List<FolderRsp>> folderList() {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dataSetFolderService.findAll(userId));
    }

    @ApiOperation("数据集-文件夹-默认文件夹")
    @GetMapping("/folder/default")
    public ApiReturn<FolderRsp> folderDefault() {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dataSetFolderService.getDefault(userId));
    }


    @ApiOperation("数据集-文件夹-创建")
    @PostMapping("/folder")
    public ApiReturn folderInsert(@Valid @RequestBody FolderReq req) {
        return ApiReturn.success(dataSetFolderService.folderInsert(req));
    }

    @ApiOperation("数据集-文件夹-修改")
    @PatchMapping("/folder/{folderId}")
    public ApiReturn folderUpdate(
            @PathVariable("folderId") Long folderId,
            @Valid @RequestBody FolderReq req) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dataSetFolderService.folderUpdate(userId, folderId, req));
    }

    @ApiOperation("数据集-文件夹-删除")
    @DeleteMapping("/folder/{folderId}")
    public ApiReturn folderDelete(@PathVariable("folderId") Long folderId, Boolean deleteData) {
        String userId = SessionHolder.getUserId();
        dataSetFolderService.folderDelete(userId, folderId, deleteData);
        return ApiReturn.success();
    }

    @ApiOperation("数据集-移动-文件夹")
    @PostMapping("/folder/{folderId}/move")
    public ApiReturn dataSetMoveFolder(@PathVariable("folderId") Long folderId, @RequestBody List<Long> datasetIds) {
        String userId = SessionHolder.getUserId();
        dataSetService.move(userId, datasetIds, folderId);
        return ApiReturn.success();
    }

}
