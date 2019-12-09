package com.plantdata.kgcloud.domain.dataset.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dataset.service.DataOptService;
import com.plantdata.kgcloud.sdk.req.DataOptQueryReq;
import com.plantdata.kgcloud.util.JacksonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-07 18:25
 **/
@Api(tags = "数据集管理")
@RestController
@RequestMapping("/dataset")
public class DataSetOptController {

    @Autowired
    private DataOptService dataOptService;

    @ApiOperation("数据集-数据-分页条件查询")
    @GetMapping("/{datasetId}/data")
    public ApiReturn<Page<Map<String, Object>>> findAll(
            @PathVariable("datasetId") Long datasetId,
            DataOptQueryReq baseReq
    ) {
        return ApiReturn.success(dataOptService.getData(datasetId, baseReq));
    }

    @ApiOperation("数据集-数据-根据Id查询")
    @GetMapping("/{datasetId}/data/{dataId}")
    public ApiReturn<Map<String, Object>> findById(
            @PathVariable("datasetId") Long datasetId,
            @PathVariable("dataId") String dataId
    ) {
        return ApiReturn.success(dataOptService.getDataById(datasetId, dataId));
    }

    @ApiOperation("数据集-数据-插入")
    @PostMapping("/{datasetId}/data")
    public ApiReturn<Map<String, Object>> insertData(
            @PathVariable("datasetId") Long datasetId,
            @RequestBody Map<String, Object> data) {
        return ApiReturn.success(dataOptService.insertData(datasetId, data));
    }

    @ApiOperation("数据集-数据-修改")
    @PatchMapping("/{datasetId}/data/{dataId}")
    public ApiReturn<Map<String, Object>> insertData(
            @PathVariable("datasetId") Long datasetId,
            @PathVariable("dataId") String dataId,
            @RequestBody Map<String, Object> data) {
        return ApiReturn.success(dataOptService.updateData(datasetId, dataId, data));
    }

    @ApiOperation("数据集-数据-根据Id删除")
    @DeleteMapping("/{datasetId}/data/{dataId}")
    public ApiReturn deleteData(
            @PathVariable("datasetId") Long datasetId,
            @PathVariable("dataId") String dataId) {
        dataOptService.deleteData(datasetId, dataId);
        return ApiReturn.success();
    }

    @ApiOperation("数据集-数据-全部删除")
    @DeleteMapping("/{datasetId}/data")
    public ApiReturn deleteAll(@PathVariable("datasetId") Long datasetId) {
        dataOptService.deleteAll(datasetId);
        return ApiReturn.success();
    }

    @ApiOperation("数据集-数据-批量删除")
    @PatchMapping("/{datasetId}/data")
    public ApiReturn batchDeleteData(
            @PathVariable("datasetId") Long datasetId,
            @RequestBody List<String> ids) {
        dataOptService.batchDeleteData(datasetId, ids);
        return ApiReturn.success();
    }


    @ApiOperation("数据集-数据-文件-导入")
    @PostMapping("/{datasetId}/upload")
    public ApiReturn upload(@PathVariable("datasetId") Long datasetId, @RequestParam(value = "file") MultipartFile file, HttpServletResponse response) {
        try {
            dataOptService.upload(datasetId, file);
        } catch (Exception e) {
            return ApiReturn.fail(KgmsErrorCodeEnum.DATASET_EXPORT_FAIL);
        }
        return ApiReturn.success();
    }

    @ApiOperation("数据集-数据-接口-导入")
    @PostMapping("/{datasetId}/import")
    public ApiReturn importData(@PathVariable("datasetId") Long datasetId, @RequestBody List<Map<String, Object>> dataList) {
        dataOptService.batchInsertData(datasetId, dataList);
        return ApiReturn.success();
    }

    @ApiOperation("数据集-数据-文件-导出")
    @GetMapping("/{datasetId}/export")
    public void exportData(@PathVariable("datasetId") Long datasetId, HttpServletResponse response) {
        try {
            dataOptService.exportData(datasetId, response);
        } catch (Exception e) {
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            try {
                ApiReturn fail = ApiReturn.fail(KgmsErrorCodeEnum.DATASET_EXPORT_FAIL);
                String error = JacksonUtils.writeValueAsString(fail);
                response.getWriter().println(error);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @ApiOperation("数据集-数据-smoke统计")
    @GetMapping("/{datasetId}/statistics")
    public ApiReturn statistics(@PathVariable("datasetId") Long datasetId) {
        dataOptService.deleteAll(datasetId);
        return ApiReturn.success();
    }

}
