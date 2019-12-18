package com.plantdata.kgcloud.domain.edit.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.edit.req.basic.SynonymReq;
import com.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author: LinHo
 * @Date: 2019/12/9 09:57
 * @Description:
 */
@Api(tags = "同义编辑")
@RestController
@RequestMapping("/edit/synonym")
public class SynonymController {

    @Autowired
    private BasicInfoService basicInfoService;

    @ApiOperation("添加概念或实体同义词")
    @PostMapping("/{kgName}/synonym/add")
    ApiReturn addSynonym(@PathVariable("kgName") String kgName,
                         @Valid @RequestBody SynonymReq synonymReq) {
        basicInfoService.addSynonym(kgName, synonymReq);
        return ApiReturn.success();
    }

    @ApiOperation("删除概念或实体同义词")
    @PostMapping("/{kgName}/synonym/delete")
    ApiReturn deleteSynonym(@PathVariable("kgName") String kgName,
                            @Valid @RequestBody SynonymReq synonymReq) {
        basicInfoService.deleteSynonym(kgName, synonymReq);
        return ApiReturn.success();
    }
}
