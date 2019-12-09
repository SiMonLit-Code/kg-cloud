package com.plantdata.kgcloud.domain.dataset.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.dataset.service.DataOptService;
import com.plantdata.kgcloud.sdk.req.DataOptQueryReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-09 17:44
 **/
//@Api(tags = "数据集管理")
//@RestController
//@RequestMapping("/dataset")
//public class DataSetAnnotationController {
//
//    @Autowired
//    private DataOptService dataOptService;
//
//    @ApiOperation("数据集-数据-分页条件查询")
//    @GetMapping("/{datasetId}/data")
//    public ApiReturn<Page<Map<String, Object>>> findAll(
//            @PathVariable("datasetId") Long datasetId,
//            DataOptQueryReq baseReq
//    ) {
//        return ApiReturn.success(dataOptService.getData(datasetId, baseReq));
//    }
//}
