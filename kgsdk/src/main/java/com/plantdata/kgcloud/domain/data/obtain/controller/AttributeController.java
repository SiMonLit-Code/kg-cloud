package com.plantdata.kgcloud.domain.data.obtain.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphDataObtainInterface;
import com.plantdata.kgcloud.sdk.EditClient;
import com.plantdata.kgcloud.sdk.KgDataClient;
import com.plantdata.kgcloud.sdk.req.app.AttrDefQueryReq;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionBatchRsp;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionModifyReq;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionReq;
import com.plantdata.kgcloud.sdk.rsp.OpenBatchResult;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionRsp;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/12 18:36
 */
@RestController
@RequestMapping("v3/kgdata/attribute")
public class AttributeController implements GraphDataObtainInterface {

    @Autowired
    private EditClient editClient;
    @Autowired
    private KgDataClient kgDataClient;


    @ApiOperation("属性定义-查询")
    @GetMapping("{kgName}")
    public ApiReturn<List<AttrDefinitionRsp>> attribute(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                        @ApiParam(value = "概念id，查询指定概念的属性 conceptId 和 conceptKey不能同时为空")
                                                        @RequestParam(value = "conceptId", required = false) Long conceptId,
                                                        @ApiParam("概念唯一标识key")
                                                        @RequestParam(value = "conceptId", required = false) String conceptKey,
                                                        @ApiParam("是否继承展示父概念属性 默认true")
                                                        @RequestParam(value = "inherit", defaultValue = "true", required = false) boolean inherit) {
        return kgDataClient.searchAttrDefByConcept(kgName, conceptId, conceptKey, inherit);
    }

    @ApiOperation("属性定义-修改")
    @PutMapping("{kgName}")
    public ApiReturn updateAttrDefinition(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                          @RequestBody AttrDefinitionModifyReq modifyReq) {
        return editClient.updateAttrDefinition(kgName, modifyReq);
    }

    @ApiOperation("属性定义-批量新增")
    @PostMapping("{kgName}/batch")
    public ApiReturn<OpenBatchResult<AttrDefinitionBatchRsp>> batchInsertAttribute(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                                                   @RequestBody List<AttrDefinitionReq> attributeList) {
        return editClient.batchAddAttrDefinition(kgName, attributeList);
    }

    @ApiOperation("属性定义批量-修改")
    @PutMapping("{kgName}/batch")
    public ApiReturn<OpenBatchResult<AttrDefinitionBatchRsp>> batchModify(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                                          @RequestBody List<AttrDefinitionModifyReq> attributeList) {
        return editClient.batchModifyAttrDefinition(kgName, attributeList);
    }

    @ApiOperation("属性定义-删除")
    @DeleteMapping("{kgName}/{id}")
    public ApiReturn delete(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                            @ApiParam(value = "属性id", required = true) @PathVariable("id") Integer id) {
        return editClient.deleteAttrDefinition(kgName, id);
    }

}
