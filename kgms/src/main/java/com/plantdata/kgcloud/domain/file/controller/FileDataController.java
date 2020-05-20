package com.plantdata.kgcloud.domain.file.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.file.req.FileDataBatchReq;
import com.plantdata.kgcloud.domain.file.req.FileDataQueryReq;
import com.plantdata.kgcloud.domain.file.req.FileDataReq;
import com.plantdata.kgcloud.domain.file.req.FileDataUpdateReq;
import com.plantdata.kgcloud.domain.file.rsq.FileDataRsp;
import com.plantdata.kgcloud.domain.file.service.FileDataService;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author lp
 * @date 2020/5/20 17:05
 */
@Api(tags = "文件数据管理")
@RestController
@RequestMapping("/file/data")
public class FileDataController {

    @Autowired
    private FileDataService fileDataService;

    @ApiOperation("文件数据管理-分页条件查询")
    @PostMapping("/list/{databaseId}/{tableId}")
    public ApiReturn<Page<FileDataRsp>> getFileData(@PathVariable("tableId") Long tableId,
                                                    @PathVariable("databaseId") Long databaseId,
                                                    @RequestBody FileDataQueryReq req) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(fileDataService.getFileData(userId, databaseId, tableId, req));
    }

    @ApiOperation("文件数据管理-文件上传")
    @PostMapping("/add")
    public ApiReturn fileAdd(@RequestBody FileDataReq req) {
        fileDataService.fileAdd(req);
        return ApiReturn.success();
    }

    @ApiOperation("文件数据管理-文件批量上传")
    @PostMapping("/add/batch")
    public ApiReturn fileAddBatch(@RequestBody FileDataBatchReq fileTableReq,
                                  MultipartFile[] files) {
        fileDataService.fileAddBatch(fileTableReq, files);
        return ApiReturn.success();
    }

    @ApiOperation("文件数据管理-编辑")
    @PostMapping("/update")
    public ApiReturn fileUpdate(@RequestBody FileDataUpdateReq req) {
        fileDataService.fileUpdate(req);
        return ApiReturn.success();
    }

    @ApiOperation("文件数据管理-删除")
    @DeleteMapping("/delete/{id}")
    public ApiReturn fileDelete(@PathVariable("id") String id) {
        fileDataService.fileDelete(id);
        return ApiReturn.success();
    }

    @ApiOperation("文件数据管理-批量删除")
    @DeleteMapping("/delete/batch")
    public ApiReturn fileDeleteBatch(@RequestBody List<String> ids) {
        fileDataService.fileDeleteBatch(ids);
        return ApiReturn.success();
    }

}
