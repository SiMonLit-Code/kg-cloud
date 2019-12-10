package com.plantdata.kgcloud.domain.dataset.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.dataset.service.DataSetAnnotationService;
import com.plantdata.kgcloud.sdk.req.AnnotationQueryReq;
import com.plantdata.kgcloud.sdk.req.AnnotationReq;
import com.plantdata.kgcloud.sdk.rsp.AnnotationRsp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-09 17:44
 **/
@Api(tags = "数据集管理")
@RestController
@RequestMapping("/dataset")
public class DataSetAnnotationController {

    @Autowired
    private DataSetAnnotationService annotationService;

    @ApiOperation("数据集-标引-分页条件查询")
    @GetMapping("/annotation/{kgName}")
    public ApiReturn<Page<AnnotationRsp>> findAll(
            @PathVariable("kgName") String kgName,
            AnnotationQueryReq baseReq
    ) {
        return ApiReturn.success(annotationService.findAll(kgName, baseReq));
    }

    @ApiOperation("数据集-标引-详情")
    @GetMapping("/annotation/{kgName}/{id}")
    public ApiReturn<AnnotationRsp> detail(
            @PathVariable("kgName") String kgName,
            @PathVariable("id") Long id
    ) {

        return ApiReturn.success(annotationService.findById(kgName, id));
    }

    @ApiOperation("数据集-标引-新增")
    @PostMapping("/annotation/{kgName}")
    public ApiReturn<AnnotationRsp> add(
            @PathVariable("kgName") String kgName,
            AnnotationReq req

    ) {
        return ApiReturn.success(annotationService.add(kgName, req));
    }

    @ApiOperation("数据集-标引-删除")
    @DeleteMapping("/annotation/{kgName}/{id}")
    public ApiReturn delete(
            @PathVariable("kgName") String kgName,
            @PathVariable("id") Long id
    ) {
        annotationService.delete(kgName, id);
        return ApiReturn.success();
    }

    @ApiOperation("数据集-标引-更新")
    @PatchMapping("/annotation/{kgName}/{id}")
    public ApiReturn<AnnotationRsp> update(
            @PathVariable("kgName") String kgName,
            @PathVariable("id") Long id,
            AnnotationReq req
    ) {
        return ApiReturn.success(annotationService.update(kgName, id, req));
    }


}
