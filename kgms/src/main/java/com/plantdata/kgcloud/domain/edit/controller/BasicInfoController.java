package com.plantdata.kgcloud.domain.edit.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.edit.req.basic.AbstractModifyReq;
import com.plantdata.kgcloud.domain.edit.req.basic.AdditionalReq;
import com.plantdata.kgcloud.sdk.req.edit.BasicInfoReq;
import com.plantdata.kgcloud.domain.edit.req.basic.ImageUrlReq;
import com.plantdata.kgcloud.domain.edit.req.basic.KgqlReq;
import com.plantdata.kgcloud.domain.edit.req.basic.PromptReq;
import com.plantdata.kgcloud.domain.edit.req.basic.SynonymReq;
import com.plantdata.kgcloud.domain.edit.rsp.BasicInfoRsp;
import com.plantdata.kgcloud.domain.edit.rsp.GraphStatisRsp;
import com.plantdata.kgcloud.domain.edit.rsp.PromptRsp;
import com.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/22 18:09
 * @Description:
 */
@Api(tags = "概念或实体编辑")
@RestController
@RequestMapping("/edit/basic")
public class BasicInfoController {

    @Autowired
    private BasicInfoService basicInfoService;

    @ApiOperation("添加概念或实体")
    @PostMapping("/{kgName}")
    ApiReturn<Long> createConcept(@PathVariable("kgName") String kgName,
                                  @Valid @RequestBody BasicInfoReq basicInfoReq) {
        return ApiReturn.success(basicInfoService.createBasicInfo(kgName, basicInfoReq));
    }

    @ApiOperation("删除概念或实体")
    @DeleteMapping("/{kgName}/{id}")
    ApiReturn deleteConcept(@PathVariable("kgName") String kgName, @PathVariable("id") Long id) {
        basicInfoService.deleteBasicInfo(kgName, id);
        return ApiReturn.success();
    }

    @ApiOperation("概念或实体详情")
    @GetMapping("/{kgName}/{id}")
    ApiReturn<BasicInfoRsp> getDetails(@PathVariable("kgName") String kgName, @PathVariable("id") Long id) {
        return ApiReturn.success(basicInfoService.getDetails(kgName, id));
    }

    @ApiOperation("修改概念或实体摘要")
    @PostMapping("/{kgName}/update/abs")
    ApiReturn updateAbstract(@PathVariable("kgName") String kgName,
                             @Valid @RequestBody AbstractModifyReq abstractModifyReq) {
        basicInfoService.updateAbstract(kgName, abstractModifyReq);
        return ApiReturn.success();
    }

    @ApiOperation("添加概念同义词")
    @PostMapping("/{kgName}/synonym/add")
    ApiReturn addSynonym(@PathVariable("kgName") String kgName,
                         @Valid @RequestBody SynonymReq synonymReq) {
        basicInfoService.addSynonym(kgName, synonymReq);
        return ApiReturn.success();
    }

    @ApiOperation("删除概念同义词")
    @PostMapping("/{kgName}/synonym/delete")
    ApiReturn deleteSynonym(@PathVariable("kgName") String kgName,
                            @Valid @RequestBody SynonymReq synonymReq) {
        basicInfoService.deleteSynonym(kgName, synonymReq);
        return ApiReturn.success();
    }

    @ApiOperation("保存图片路径")
    @PostMapping("/{kgName}/image/url")
    ApiReturn saveImageUrl(@PathVariable("kgName") String kgName,
                           @Valid @RequestBody ImageUrlReq imageUrlReq) {
        basicInfoService.saveImageUrl(kgName, imageUrlReq);
        return ApiReturn.success();
    }

    @ApiOperation("概念实体同义属性提示")
    @GetMapping("/{kgName}/prompt")
    ApiReturn<List<PromptRsp>> prompt(@PathVariable("kgName") String kgName, PromptReq promptReq) {
        return ApiReturn.success(basicInfoService.prompt(kgName, promptReq));
    }

    @ApiOperation("图谱统计")
    @GetMapping("/{kgName}/statis")
    ApiReturn<GraphStatisRsp> graphStatis(@PathVariable("kgName") String kgName) {
        return ApiReturn.success(basicInfoService.graphStatis(kgName));
    }

    @ApiOperation("批量保存额外信息")
    @PostMapping("/{kgName}/batch/additional/save")
    ApiReturn batchAddMetaData(@PathVariable("kgName") String kgName,
                               @Valid @RequestBody AdditionalReq additionalReq) {
        basicInfoService.batchAddMetaData(kgName, additionalReq);
        return ApiReturn.success();
    }

    @ApiOperation("一键清空额外信息")
    @DeleteMapping("/{kgName}/additional/clear")
    ApiReturn clearMetaData(@PathVariable("kgName") String kgName) {
        basicInfoService.clearMetaData(kgName);
        return ApiReturn.success();
    }

    @ApiOperation("一键清空额外信息")
    @PostMapping("/execute/kgql")
    ApiReturn executeQl(@Valid @RequestBody KgqlReq kgqlReq) {
        basicInfoService.executeQl(kgqlReq);
        return ApiReturn.success();
    }
}
