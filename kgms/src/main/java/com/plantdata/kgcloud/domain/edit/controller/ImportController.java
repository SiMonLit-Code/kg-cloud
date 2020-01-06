package com.plantdata.kgcloud.domain.edit.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.CommonErrorCode;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.edit.req.upload.ImportTemplateReq;
import com.plantdata.kgcloud.domain.edit.service.ImportService;
import com.plantdata.kgcloud.exception.BizException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ApiReturn importConcepts(@PathVariable("kgName") String kgName,
                                    @ApiParam(required = true) MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ApiReturn.fail(CommonErrorCode.BAD_REQUEST);
        }
        checkFileType(file);
        return ApiReturn.success(importService.importConcepts(kgName, file));
    }

    @ApiOperation("实体导入(包含数值属性值)")
    @PostMapping("/{kgName}/{conceptId}/entity")
    public ApiReturn importEntities(@PathVariable("kgName") String kgName,
                                    @PathVariable("conceptId") Long conceptId,
                                    @ApiParam(required = true) MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ApiReturn.fail(CommonErrorCode.BAD_REQUEST);
        }
        checkFileType(file);
        return ApiReturn.success(importService.importEntities(kgName, conceptId, file));
    }

    @ApiOperation("同义词导入")
    @PostMapping("/{kgName}/synonym")
    public ApiReturn importSynonyms(@PathVariable("kgName") String kgName,
                                    @ApiParam(required = true) MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ApiReturn.fail(CommonErrorCode.BAD_REQUEST);
        }
        checkFileType(file);
        return ApiReturn.success(importService.importSynonyms(kgName, file));
    }

    @ApiOperation("属性定义导入")
    @PostMapping("/{kgName}/attr/{type}")
    public ApiReturn importAttrDefinition(@PathVariable("kgName") String kgName,
                                          @PathVariable("type") Integer type,
                                          @ApiParam(required = true) MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ApiReturn.fail(CommonErrorCode.BAD_REQUEST);
        }
        checkFileType(file);
        return ApiReturn.success(importService.importAttrDefinition(kgName, type, file));
    }

    @ApiOperation("领域词导入")
    @PostMapping("/{kgName}/{conceptId}/domain")
    public ApiReturn importDomain(@PathVariable("kgName") String kgName,
                                  @PathVariable("conceptId") Long conceptId,
                                  @ApiParam(required = true) MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ApiReturn.fail(CommonErrorCode.BAD_REQUEST);
        }
        checkFileType(file);
        return ApiReturn.success(importService.importDomain(kgName, conceptId, file));
    }

    @ApiOperation("关系导入")
    @PostMapping("/{kgName}/{mode}/relation")
    public ApiReturn importRelation(@PathVariable("kgName") String kgName,
                                    @ApiParam(value = "0:忽略不存在的实例,1:实体不存在则新增", required = true)
                                    @PathVariable("mode") Integer mode,
                                    @ApiParam(required = true) MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ApiReturn.fail(CommonErrorCode.BAD_REQUEST);
        }
        checkFileType(file);
        return ApiReturn.success(importService.importRelation(kgName, mode, file));
    }

    @ApiOperation("特定关系导入")
    @PostMapping("/{kgName}/{attrId}/{mode}/relation")
    public ApiReturn importRelation(@PathVariable("kgName") String kgName,
                                    @PathVariable("attrId") Integer attrId,
                                    @PathVariable("mode") Integer mode,
                                    @ApiParam(required = true) MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ApiReturn.fail(CommonErrorCode.BAD_REQUEST);
        }
        checkFileType(file);
        return ApiReturn.success(importService.importRelation(kgName, attrId, mode, file));
    }

    @ApiOperation("rdf导入 ")
    @PostMapping("/{kgName}/rdf")
    public ApiReturn importRdf(@PathVariable("kgName") String kgName,
                               @RequestParam("format") String format,
                               @ApiParam(required = true) MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ApiReturn.fail(CommonErrorCode.BAD_REQUEST);
        }
        return ApiReturn.success(importService.importRdf(kgName, file, format));
    }

    @ApiOperation("rdf导出 ")
    @GetMapping("/{kgName}/rdf/{format}/{scope}")
    public ApiReturn exportRdf(@PathVariable("kgName") String kgName,
                               @PathVariable("format") String format,
                               @PathVariable("scope") Integer scope) {
        return ApiReturn.success(importService.exportRdf(kgName, format, scope));
    }

    /**
     * 校验文件类型
     *
     * @param file
     */
    private void checkFileType(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (!StringUtils.hasText(filename)) {
            throw BizException.of(CommonErrorCode.BAD_REQUEST);
        }
        String suffix = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        if (!(suffix.equals("xlsx") || suffix.equals("xls"))) {
            throw BizException.of(KgmsErrorCodeEnum.FILE_TYPE_ERROR);
        }
    }
}
