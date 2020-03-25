package com.plantdata.kgcloud.domain.dw.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dw.req.PreBuilderCreateReq;
import com.plantdata.kgcloud.domain.dw.rsp.PreBuilderSearchRsp;
import com.plantdata.kgcloud.domain.dw.service.PreBuilderService;
import com.plantdata.kgcloud.sdk.req.PreBuilderSearchReq;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-03-16 16:43
 **/
@Api(tags = "预构建模式管理")
@RestController
@RequestMapping("/builder/manage")
public class PreBuildManageController {

    @Autowired
    private PreBuilderService preBuilderService;


    @ApiOperation("预构建模式管理-列表")
    @PostMapping("/list")
    public ApiReturn<Page<PreBuilderSearchRsp>> listManage(@RequestBody PreBuilderSearchReq preBuilderSearchReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(preBuilderService.listManage(userId,preBuilderSearchReq));
    }

    @ApiOperation("预构建模式管理-删除")
    @PatchMapping("/delete/{id}")
    public ApiReturn delete(@PathVariable("id")Integer id) {
        String userId = SessionHolder.getUserId();
        preBuilderService.delete(userId,id);
        return ApiReturn.success();
    }

    @ApiOperation("预构建模式管理-发布(1)/禁用(0)")
    @PatchMapping("/update/{id}/{status}")
    public ApiReturn update(@PathVariable("id")Integer id,
                            @PathVariable("status")String status) {
        String userId = SessionHolder.getUserId();
        preBuilderService.update(userId,id,status);
        return ApiReturn.success();
    }


    @ApiOperation("预构建模式管理-模式上传")
    @PostMapping("/create")
    public ApiReturn create(@RequestBody PreBuilderCreateReq req,
            @RequestParam(value = "file") MultipartFile file) {
        long size = file.getSize();
        if (size > 20 * 1024 * 1024) {
            return ApiReturn.fail(KgmsErrorCodeEnum.FILE_OUT_LIMIT);
        }
        try {
            preBuilderService.create(req, file);
            return ApiReturn.success();
        } catch (Exception e) {
            return ApiReturn.fail(KgmsErrorCodeEnum.DATASET_IMPORT_FAIL);
        }

    }

}
