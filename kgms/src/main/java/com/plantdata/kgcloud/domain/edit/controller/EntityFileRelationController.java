package com.plantdata.kgcloud.domain.edit.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.edit.entity.EntityFileRelation;
import com.plantdata.kgcloud.domain.edit.req.file.EntityFileRelationQueryReq;
import com.plantdata.kgcloud.domain.edit.req.file.EntityFileRelationReq;
import com.plantdata.kgcloud.domain.edit.rsp.EntityFileRelationRsp;
import com.plantdata.kgcloud.domain.edit.service.EntityFileRelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("实体文件管理-建立关系")
    @PostMapping("/{kgName}/create/relation")
    public ApiReturn createRelation(@PathVariable String kgName,
                                    @Valid @RequestBody EntityFileRelationReq req) {
        entityFileRelationService.createRelation(kgName, req);
        return ApiReturn.success();
    }

    @ApiOperation("实体文件管理-更新关系")
    @PostMapping("/{kgName}/update/relation")
    public ApiReturn updateRelation(@PathVariable String kgName,
                                    @RequestBody EntityFileRelation req) {
        entityFileRelationService.updateRelation(req);
        return ApiReturn.success();
    }

    @ApiOperation("实体文件管理-批量删除关系")
    @PostMapping("/{kgName}/delete/relation")
    public ApiReturn deleteRelation(@PathVariable String kgName,
                                    @RequestBody List<Integer> idList) {
        entityFileRelationService.deleteRelation(idList);
        return ApiReturn.success();
    }

    @ApiOperation("实体文件管理-根据数仓文件ID删除关系")
    @PostMapping("/{kgName}/delete/dwFileId/{dwFileId}")
    public ApiReturn deleteRelationByDwFileId(@PathVariable String kgName,
                                              @PathVariable Integer dwFileId) {
        entityFileRelationService.deleteRelationByDwFileId(dwFileId);
        return ApiReturn.success();
    }

    @ApiOperation("实体文件管理-根据多模态文件ID删除关系")
    @PostMapping("/{kgName}/delete/multiModalId/{multiModalId}")
    public ApiReturn deleteRelationByMultiModalId(@PathVariable String kgName,
                                                  @PathVariable String multiModalId) {
        entityFileRelationService.deleteRelationByMultiModalId(multiModalId);
        return ApiReturn.success();
    }

    @ApiOperation("实体文件管理-根据数仓文件ID获取关系")
    @PostMapping("/{kgName}/get/dwFileId/{dwFileId}")
    public ApiReturn<EntityFileRelation> getRelationByDwFileId(@PathVariable String kgName,
                                                               @PathVariable Integer dwFileId) {
        return ApiReturn.success(entityFileRelationService.getRelationByDwFileId(dwFileId));
    }

    @ApiOperation("实体文件管理-根据多模态文件ID获取关系")
    @PostMapping("/{kgName}/get/multiModalId/{multiModalId}")
    public ApiReturn<EntityFileRelation> getRelationByMultiModalId(@PathVariable String kgName,
                                                                   @PathVariable String multiModalId) {
        return ApiReturn.success(entityFileRelationService.getRelationByMultiModalId(multiModalId));
    }

    @ApiOperation("实体文件管理-查询关系列表")
    @PostMapping("/{kgName}/list/relation")
    public ApiReturn<Page<EntityFileRelationRsp>> listRelation(@PathVariable String kgName,
                                                               @RequestBody EntityFileRelationQueryReq req) {
        return ApiReturn.success(entityFileRelationService.listRelation(kgName, req));
    }

}
