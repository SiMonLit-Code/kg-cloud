package com.plantdata.kgcloud.domain.prebuilder.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.constant.SchemaType;
import com.plantdata.kgcloud.domain.prebuilder.aop.PostHandler;
import com.plantdata.kgcloud.domain.prebuilder.req.*;
import com.plantdata.kgcloud.domain.prebuilder.rsp.PreBuilderSearchRsp;
import com.plantdata.kgcloud.domain.prebuilder.service.PreBuilderService;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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
    @PostHandler(id = 12021011)
    public ApiReturn<Page<PreBuilderSearchRsp>> listManage(@RequestBody PreBuilderSearchReq preBuilderSearchReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(preBuilderService.listManage(userId, preBuilderSearchReq));
    }

    @ApiOperation("预构建模式管理-删除")
    @PostMapping("/delete")
    @PostHandler(id = 12021012)
    public ApiReturn delete(@RequestBody PreBuilderDeleteReq req) {
        if(SchemaType.isKG(req.getSchemaType())){
            String userId = SessionHolder.getUserId();
            preBuilderService.delete(userId, req.getModelId());
        }
        return ApiReturn.success();
    }

    @ApiOperation("预构建模式管理-发布(1)/禁用(0)")
    @PostMapping("/update/status")
    @PostHandler(id = 12021013)
    public ApiReturn update(@RequestBody PreBuilderUpdateStatusReq req) {
        if(SchemaType.isKG(req.getSchemaType())){
            String userId = SessionHolder.getUserId();
            preBuilderService.update(userId, req.getModelId(), req.getStatus());
        }
        return ApiReturn.success();
    }


    @ApiOperation("预构建模式管理-模式上传")
    @PostMapping("/create")
    @PostHandler(id = 12021014)
    public ApiReturn create(@RequestBody PreBuilderCreateReq req) {
        String errorFilePath = preBuilderService.create(req);
        if("".equals(errorFilePath)){
            return ApiReturn.success();
        }else {
            return ApiReturn.fail(KgmsErrorCodeEnum.MODEL_PARSER_ERROR.getErrorCode(),errorFilePath);
        }
    }

    @ApiOperation("预构建模式管理-模式编辑")
    @PostMapping("/update")
    @PostHandler(id = 12021015)
    public ApiReturn update(@RequestBody PreBuilderUpdateReq req) {
        if(SchemaType.isKG(req.getSchemaType())){
            preBuilderService.updateModel(req);
        }
        return ApiReturn.success();
    }

}
