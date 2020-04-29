package com.plantdata.kgcloud.domain.edit.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.constant.CommonErrorCode;
import com.plantdata.kgcloud.domain.edit.req.file.EntityFileRelationQueryReq;
import com.plantdata.kgcloud.domain.edit.req.file.EntityFileRelationReq;
import com.plantdata.kgcloud.domain.edit.req.file.IndexRelationReq;
import com.plantdata.kgcloud.domain.edit.rsp.DWFileRsp;
import com.plantdata.kgcloud.domain.edit.rsp.EntityFileRelationRsp;
import com.plantdata.kgcloud.domain.edit.service.EntityFileRelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * @author EYE
 */
@Api(tags = "实体文件管理")
@RestController
@RequestMapping("/entity/file")
public class EntityFileRelationController {

    @Autowired
    private EntityFileRelationService entityFileRelationService;

    @ApiOperation("实体文件管理-查询关系列表")
    @PostMapping("/{kgName}/list/relation")
    public ApiReturn<Page<DWFileRsp>> listRelation(@PathVariable String kgName,
                                                   @RequestBody EntityFileRelationQueryReq req) {
        return ApiReturn.success(entityFileRelationService.listRelation(kgName, req));
    }

    @ApiOperation("实体文件管理-建立关系")
    @PostMapping("/{kgName}/create/relation")
    public ApiReturn createRelation(@PathVariable String kgName,
                                    @Valid @RequestBody EntityFileRelationReq req) {
        entityFileRelationService.createRelation(kgName, req);
        return ApiReturn.success();
    }

    @ApiOperation("实体文件管理-批量删除关系")
    @PostMapping("/{kgName}/delete/relation")
    public ApiReturn deleteRelation(@PathVariable String kgName,
                                    @RequestBody List<String> idList) {
        entityFileRelationService.deleteRelation(idList);
        return ApiReturn.success();
    }

    @ApiOperation("实体文件管理-根据数仓文件ID删除关系")
    @PostMapping("/{kgName}/delete/dwFileId/{dwFileId}")
    public ApiReturn deleteRelationByDwFileId(@PathVariable String kgName,
                                              @PathVariable String dwFileId) {
        entityFileRelationService.deleteRelationByDwFileId(dwFileId);
        return ApiReturn.success();
    }

    @ApiOperation("实体文件管理-根据数仓文件ID获取关系")
    @PostMapping("/{kgName}/get/dwFileId/{dwFileId}")
    public ApiReturn<List<EntityFileRelationRsp>> getRelationByDwFileId(@PathVariable String kgName,
                                                                        @PathVariable String dwFileId) {
        return ApiReturn.success(entityFileRelationService.getRelationByDwFileId(dwFileId));
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
                                    @Valid @RequestBody IndexRelationReq req) {
        entityFileRelationService.updateIndex(kgName, req);
        return ApiReturn.success();
    }

}
