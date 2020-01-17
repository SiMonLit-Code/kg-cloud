package com.plantdata.kgcloud.domain.dataset.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.dataset.service.DataSetFolderService;
import com.plantdata.kgcloud.domain.dataset.service.DataSetService;
import com.plantdata.kgcloud.sdk.KgtextClient;
import com.plantdata.kgcloud.sdk.req.DataSetCreateReq;
import com.plantdata.kgcloud.sdk.req.DataSetPageReq;
import com.plantdata.kgcloud.sdk.req.DataSetPdReq;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.sdk.req.DataSetSdkReq;
import com.plantdata.kgcloud.sdk.req.DataSetUpdateReq;
import com.plantdata.kgcloud.sdk.req.FolderReq;
import com.plantdata.kgcloud.sdk.rsp.CorpusSetRsp;
import com.plantdata.kgcloud.sdk.rsp.DataSetRsp;
import com.plantdata.kgcloud.sdk.rsp.DataSetUpdateRsp;
import com.plantdata.kgcloud.sdk.rsp.FolderRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private KgtextClient kgtextClient;

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
    public ApiReturn<DataSetUpdateRsp> findById(@PathVariable Long id) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dataSetService.findById(userId, id));
    }

    @ApiOperation("数据集-schema-创建")
    @PostMapping("/")
    public ApiReturn<DataSetRsp> insert(@Valid @RequestBody DataSetCreateReq req) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dataSetService.insert(userId, req));
    }

    @ApiOperation("数据集-schema-文本数据集列表")
    @GetMapping("/text/list")
    public ApiReturn<List<CorpusSetRsp>> listAll() {
        String userId = SessionHolder.getUserId();
        return kgtextClient.listAll(userId);
    }

    @ApiOperation("数据集-schema-创建文本数据集")
    @PostMapping("/create/text")
    public ApiReturn<DataSetRsp> insert(@Valid @RequestBody DataSetPdReq req) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dataSetService.insert(userId, req));
    }


    @ApiOperation("数据集-schema-编辑")
    @PatchMapping("/{id}")
    public ApiReturn<DataSetRsp> update(@PathVariable Long id, @Valid @RequestBody DataSetUpdateReq req) {
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

    @ApiOperation("数据集-schema-识别")
    @PostMapping("/schema")
    public ApiReturn<List<DataSetSchema>> resolve(@RequestParam(value = "dataType") Integer dataType, @RequestParam(value = "file") MultipartFile file) {
        return ApiReturn.success(dataSetService.schemaResolve(dataType, file));
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
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dataSetFolderService.folderInsert(userId,req));
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
    public ApiReturn folderDelete(
            @PathVariable("folderId") Long folderId,
            @ApiParam("是否删除对应数据")
            @RequestParam("deleteData") Boolean deleteData) {
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



    @ApiOperation("数据集-schema-根据 dataNames 查询ids")
    @PostMapping("/dataname")
    public ApiReturn<List<Long>> findByDataName(@RequestBody List<String> dataNames) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dataSetService.findByDataNames(userId, dataNames));
    }

    @ApiOperation("数据集-schema-根据 dbName tbName 查询ids")
    @PostMapping("/database")
    public ApiReturn<List<Long>> findByDatabase(@RequestBody List<DataSetSdkReq> dataNames) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dataSetService.findByDatabase(userId, dataNames));
    }
}
