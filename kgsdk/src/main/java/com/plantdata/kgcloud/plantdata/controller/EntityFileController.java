package com.plantdata.kgcloud.plantdata.controller;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.plantdata.req.data.EntityFileRelationParameter;
import com.plantdata.kgcloud.sdk.EntityFileClient;
import com.plantdata.kgcloud.sdk.req.EntityFileRelationAddReq;
import com.plantdata.kgcloud.sdk.rsp.edit.EntityFileRelationRsp;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Arrays;

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
    @ApiOperation("实体文件管理-建立文件标引关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "fileId", required = true, dataType = "string", paramType = "form", value = "文件id"),
            @ApiImplicitParam(name = "entityIds", required = true, dataType = "string", paramType = "form", value = "实体id列表"),
    })
    public RestResp<EntityFileRelationRsp> add(@Valid @ApiIgnore EntityFileRelationParameter param) {
        EntityFileRelationAddReq req = new EntityFileRelationAddReq();
        req.setFileId(param.getFileId());
        req.setEntityIds(param.getEntityIds());
        ApiReturn<EntityFileRelationRsp> entityFileRelationRspApiReturn = entityFileClient.add(param.getKgName(), req);
        return new RestResp<>(entityFileRelationRspApiReturn.getData());
    }
}
