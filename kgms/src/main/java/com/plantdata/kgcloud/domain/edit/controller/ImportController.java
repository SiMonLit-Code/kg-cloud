package com.plantdata.kgcloud.domain.edit.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.CommonErrorCode;
import com.plantdata.kgcloud.domain.edit.req.upload.ImportTemplateReq;
import com.plantdata.kgcloud.domain.edit.service.ImportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: LinHo
 * @Date: 2019/12/2 10:01
 * @Description:
 */
@Api(tags = "概念,实体等导入")
@RestController
@RequestMapping("/import")
public class ImportController {

    @Autowired
    private ImportService importService;

    @ApiOperation("模本文件下载")
    @GetMapping("/{kgName}/template")
    public ApiReturn getImportTemplate(@PathVariable("kgName") String kgName,
                                       ImportTemplateReq importTemplateReq,
                                       HttpServletResponse response) {
        importService.getImportTemplate(kgName, importTemplateReq, response);
        return ApiReturn.success();
    }

    @ApiOperation("概念导入")
    @PostMapping("/{kgName}/concept")
    public ApiReturn importConcepts(@PathVariable("kgName") String kgName, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ApiReturn.fail(CommonErrorCode.BAD_REQUEST);
        }
        return ApiReturn.success(importService.importConcepts(kgName, file));
    }

    @ApiOperation("实体导入(包含数值属性值)")
    @PostMapping("/{kgName}/{conceptId}/entity")
    public ApiReturn importEntities(@PathVariable("kgName") String kgName,
                                    @PathVariable("conceptId") Long conceptId,
                                    MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ApiReturn.fail(CommonErrorCode.BAD_REQUEST);
        }
        return ApiReturn.success(importService.importEntities(kgName, conceptId, file));
    }

    @ApiOperation("同义词导入")
    @PostMapping("/{kgName}/synonym")
    public ApiReturn importSynonyms(@PathVariable("kgName") String kgName, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ApiReturn.fail(CommonErrorCode.BAD_REQUEST);
        }
        return ApiReturn.success(importService.importSynonyms(kgName, file));
    }

    @ApiOperation("属性定义导入")
    @PostMapping("/{kgName}/attr/{type}")
    public ApiReturn importAttrDefinition(@PathVariable("kgName") String kgName,
                                          @PathVariable("type") Integer type,
                                          MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ApiReturn.fail(CommonErrorCode.BAD_REQUEST);
        }
        return ApiReturn.success(importService.importAttrDefinition(kgName, type, file));
    }

    @ApiOperation("领域词导入")
    @PostMapping("/{kgName}/{conceptId}/domain")
    public ApiReturn importDomain(@PathVariable("kgName") String kgName,
                                  @PathVariable("conceptId") Long conceptId,
                                  MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ApiReturn.fail(CommonErrorCode.BAD_REQUEST);
        }
        return ApiReturn.success(importService.importDomain(kgName, conceptId, file));
    }

    @ApiOperation("关系导入")
    @PostMapping("/{kgName}/{mode}/relation")
    public ApiReturn importRelation(@PathVariable("kgName") String kgName,
                                    @PathVariable("mode") Integer mode,
                                    MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ApiReturn.fail(CommonErrorCode.BAD_REQUEST);
        }
        return ApiReturn.success(importService.importRelation(kgName, mode, file));
    }

    @ApiOperation("特定关系导入")
    @PostMapping("/{kgName}/{attrId}/{mode}/relation")
    public ApiReturn importRelation(@PathVariable("kgName") String kgName,
                                    @PathVariable("attrId") Integer attrId,
                                    @PathVariable("mode") Integer mode,
                                    MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ApiReturn.fail(CommonErrorCode.BAD_REQUEST);
        }
        return ApiReturn.success(importService.importRelation(kgName, attrId, mode, file));
    }
}
