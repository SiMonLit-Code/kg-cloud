package com.plantdata.kgcloud.plantdata.controller;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.plantdata.req.data.EntityFileRelationParameter;
import com.plantdata.kgcloud.sdk.EntityFileClient;
import com.plantdata.kgcloud.sdk.req.EntityFileRelationAddReq;
import com.plantdata.kgcloud.sdk.rsp.edit.EntityFileRelationRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * @author lp
 * @date 2020/5/21 21:23
 */
@RestController
@RequestMapping("sdk/entity/file")
public class EntityFileController {

    @Autowired
    private EntityFileClient entityFileClient;

    @PostMapping("add")
    @ApiOperation("实体文件管理-建立标引关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "indexType", required = true, dataType = "int", paramType = "form", value = "标引类型(0：文件标引，1：文本标引，3：链接标引)"),
            @ApiImplicitParam(name = "entityIds", required = true, dataType = "string", paramType = "form", value = "实体id列表"),
            @ApiImplicitParam(name = "fileId", dataType = "string", paramType = "form", value = "文件id(文件标引必需)"),
            @ApiImplicitParam(name = "title", dataType = "string", paramType = "form", value = "标题(文本、链接标引必需)"),
            @ApiImplicitParam(name = "keyword", dataType = "string", paramType = "form", value = "关键词"),
            @ApiImplicitParam(name = "description", dataType = "string", paramType = "form", value = "简介(文本标引必需)"),
            @ApiImplicitParam(name = "url", dataType = "string", paramType = "form", value = "链接(链接标引必需)"),
    })
    public RestResp<EntityFileRelationRsp> add(@Valid @ApiIgnore EntityFileRelationParameter param) {
        EntityFileRelationAddReq req = ConvertUtils.convert(EntityFileRelationAddReq.class).apply(param);
        ApiReturn<EntityFileRelationRsp> apiReturn = entityFileClient.add(param.getKgName(), req);
        if (apiReturn.getErrCode() == 200) {
            return new RestResp<>(apiReturn.getData());
        } else {
            return new RestResp<>(apiReturn.getErrCode(), apiReturn.getMessage());
        }
    }
}
