package com.plantdata.kgcloud.domain.edit.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.edit.aop.EditPermissionUnwanted;
import com.plantdata.kgcloud.domain.edit.req.basic.AbstractModifyReq;
import com.plantdata.kgcloud.domain.edit.req.basic.AdditionalReq;
import com.plantdata.kgcloud.domain.edit.req.basic.BasicReq;
import com.plantdata.kgcloud.domain.edit.req.basic.ImageUrlReq;
import com.plantdata.kgcloud.domain.edit.req.basic.PromptReq;
import com.plantdata.kgcloud.domain.edit.rsp.GraphStatisRsp;
import com.plantdata.kgcloud.domain.edit.rsp.PromptRsp;
import com.plantdata.kgcloud.domain.edit.rsp.RelationDetailRsp;
import com.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import com.plantdata.kgcloud.sdk.req.edit.BasicInfoReq;
import com.plantdata.kgcloud.sdk.req.edit.KgqlReq;
import com.plantdata.kgcloud.sdk.rsp.BasicInfoRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.SimpleBasicRsp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
    public ApiReturn<Long> createConcept(@PathVariable("kgName") String kgName,
                                         @Valid @RequestBody BasicInfoReq basicInfoReq) {
        return ApiReturn.success(basicInfoService.createBasicInfo(kgName, basicInfoReq));
    }

    @ApiOperation("删除概念或实体")
    @DeleteMapping("/{kgName}/{id}")
    public ApiReturn deleteConcept(@PathVariable("kgName") String kgName, @PathVariable("id") Long id,
                                   @RequestParam(defaultValue = "false", required = false) Boolean force) {
        basicInfoService.deleteBasicInfo(kgName, id, force);
        return ApiReturn.success();
    }

    @ApiOperation("概念或实体详情")
    @GetMapping("/{kgName}/detail")
    public ApiReturn<BasicInfoRsp> getDetails(@PathVariable("kgName") String kgName,
                                              @Valid BasicReq basicReq) {
        return ApiReturn.success(basicInfoService.getDetails(kgName, basicReq));
    }

    @ApiOperation("修改概念或实体摘要")
    @PostMapping("/{kgName}/update/abs")
    public ApiReturn updateAbstract(@PathVariable("kgName") String kgName,
                                    @Valid @RequestBody AbstractModifyReq abstractModifyReq) {
        basicInfoService.updateAbstract(kgName, abstractModifyReq);
        return ApiReturn.success();
    }

    @ApiOperation("保存图片路径")
    @PostMapping("/{kgName}/image/url")
    public ApiReturn saveImageUrl(@PathVariable("kgName") String kgName,
                                  @Valid @RequestBody ImageUrlReq imageUrlReq) {
        basicInfoService.saveImageUrl(kgName, imageUrlReq);
        return ApiReturn.success();
    }

    @ApiOperation("概念实体同义属性提示")
    @PostMapping("/{kgName}/prompt")
    public ApiReturn<List<PromptRsp>> prompt(@PathVariable("kgName") String kgName, @RequestBody PromptReq promptReq) {
        return ApiReturn.success(basicInfoService.prompt(kgName, promptReq));
    }

    @ApiOperation("图谱统计")
    @GetMapping("/{kgName}/statis")
    public ApiReturn<GraphStatisRsp> graphStatis(@PathVariable("kgName") String kgName) {
        return ApiReturn.success(basicInfoService.graphStatis(kgName));
    }

    @ApiOperation("批量保存额外信息")
    @PostMapping("/{kgName}/batch/additional/save")
    public ApiReturn batchAddMetaData(@PathVariable("kgName") String kgName,
                                      @Valid @RequestBody AdditionalReq additionalReq) {
        basicInfoService.batchAddMetaData(kgName, additionalReq);
        return ApiReturn.success();
    }

    @ApiOperation("一键清空额外信息")
    @DeleteMapping("/{kgName}/additional/clear")
    public ApiReturn clearMetaData(@PathVariable("kgName") String kgName) {
        basicInfoService.clearMetaData(kgName);
        return ApiReturn.success();
    }

    @ApiOperation("kgql")
    @PostMapping("/execute/kgql")
    @EditPermissionUnwanted
    public ApiReturn executeQl(@Valid @RequestBody KgqlReq kgqlReq) {
        return ApiReturn.success(basicInfoService.executeQl(kgqlReq));
    }

    @ApiOperation("根据批量名称查询实体")
    @PostMapping("/{kgName}/list/name")
    public ApiReturn<List<SimpleBasicRsp>> listNames(@PathVariable("kgName") String kgName,
                                                     @Valid @Min(1) @Max(10000) @RequestBody List<String> names) {
        return ApiReturn.success(basicInfoService.listNames(kgName, names));
    }

    @ApiOperation("根据三元组id查关系详情")
    @GetMapping("/relation/{kgName}/{id}")
    public ApiReturn<RelationDetailRsp> getRelationDetails(@PathVariable("kgName") String kgName, @PathVariable("id") String id) {
        return ApiReturn.success(basicInfoService.getRelationDetails(kgName, id));
    }
}
