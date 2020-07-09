package com.plantdata.kgcloud.domain.edit.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.constant.CommonErrorCode;
import com.plantdata.kgcloud.domain.edit.entity.EntityFileRelation;
import com.plantdata.kgcloud.domain.edit.req.file.EntityFileRelationQueryReq;
import com.plantdata.kgcloud.domain.edit.req.file.IndexRelationReq;
import com.plantdata.kgcloud.domain.edit.service.EntityFileRelationService;
import com.plantdata.kgcloud.sdk.req.EntityFileRelationAddReq;
import com.plantdata.kgcloud.sdk.rsp.edit.EntityFileRelationRsp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author lp
 */
@Api(tags = "实体文件管理")
@RestController
@RequestMapping("/entity/file")
public class EntityFileRelationController {

    @Autowired
    private EntityFileRelationService entityFileRelationService;

    @ApiOperation("实体文件管理-查询关系列表")
    @PostMapping("/{kgName}/list/relation")
    public ApiReturn<Page<EntityFileRelationRsp>> listRelation(@PathVariable String kgName,
                                                               @RequestBody EntityFileRelationQueryReq req) {
        return ApiReturn.success(entityFileRelationService.listRelation(kgName, req));
    }

    @ApiOperation("实体文件管理-根据文件ID删除关系")
    @PostMapping("/{kgName}/delete/fileId/{fileId}")
    public ApiReturn deleteRelationByFileId(@PathVariable String kgName,
                                            @PathVariable String fileId) {
        entityFileRelationService.deleteRelationByFileId(fileId);
        return ApiReturn.success();
    }

    @ApiOperation("实体文件管理-根据文件ID获取关系")
    @PostMapping("/{kgName}/get/fileId/{fileId}")
    public ApiReturn<EntityFileRelation> getRelationByDwFileId(@PathVariable String kgName,
                                                               @PathVariable String fileId) {
        return ApiReturn.success(entityFileRelationService.getRelationByFileId(kgName, fileId));
    }

    @ApiOperation("实体文件管理-新建标引")
    @PostMapping("/{kgName}/add/index/{indexType}")
    public ApiReturn addIndex(@PathVariable String kgName,
                              @PathVariable Integer indexType,
                              @ApiParam(required = true) MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ApiReturn.fail(CommonErrorCode.BAD_REQUEST);
        }
        entityFileRelationService.addIndex(kgName, indexType, file);
        return ApiReturn.success();
    }

    @ApiOperation("实体文件管理-标引实体")
    @PostMapping("/{kgName}/update/index")
    public ApiReturn updateIndex(@PathVariable String kgName,
                                 @RequestBody IndexRelationReq req) {
        entityFileRelationService.updateIndex(kgName, req);
        return ApiReturn.success();
    }

    @ApiOperation("实体文件管理-取消标引")
    @PostMapping("/{kgName}/cancel/index")
    public ApiReturn deleteRelation(@PathVariable String kgName,
                                    @RequestBody IndexRelationReq req) {
        entityFileRelationService.cancelIndex(kgName, req);
        return ApiReturn.success();
    }

    @ApiOperation("实体文件管理-批量删除标引")
    @PostMapping("/{kgName}/delete/index")
    public ApiReturn deleteIndex(@PathVariable String kgName,
                                 @RequestBody List<String> relationIds) {
        entityFileRelationService.deleteIndexByIds(kgName, relationIds);
        return ApiReturn.success();
    }

    @ApiOperation("实体文件管理-新增文件标引")
    @PostMapping("/{kgName}/add/file/{fileSystemId}/{folderId}")
    public ApiReturn addFile(@PathVariable String kgName,
                             @PathVariable Long fileSystemId,
                             @PathVariable Long folderId) {
        entityFileRelationService.addFile(kgName, fileSystemId, folderId);
        return ApiReturn.success();
    }

    @ApiOperation("实体文件管理-批量建立标引关系")
    @PostMapping("/{kgName}/create/relation")
    public ApiReturn<EntityFileRelationRsp> createRelation(@PathVariable String kgName,
                                                           @RequestBody EntityFileRelationAddReq req) {
        return ApiReturn.success(entityFileRelationService.createRelation(kgName, req));
    }

}
