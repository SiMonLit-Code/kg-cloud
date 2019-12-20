package com.plantdata.kgcloud.domain.data.obtain.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphDataObtainInterface;
import com.plantdata.kgcloud.sdk.EditClient;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionBatchRsp;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionModifyReq;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionReq;
import com.plantdata.kgcloud.sdk.rsp.OpenBatchResult;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionConceptsReq;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation("属性定义批量-修改")
    @PatchMapping("batchModify/{kgName}")
    public ApiReturn<OpenBatchResult<AttrDefinitionBatchRsp>> batchModify(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                                          @RequestBody List<AttrDefinitionReq> attributeList) {
        return editClient.batchModifyAttrDefinition(kgName, attributeList);
    }

    @ApiOperation("属性定义-删除")
    @DeleteMapping("{kgName}/{id}")
    public ApiReturn delete(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                            @ApiParam(value = "属性id", required = true) @PathVariable("id") Integer id) {
        return editClient.deleteAttrDefinition(kgName, id);
    }

    @ApiOperation("属性定义-查询")
    @GetMapping("{kgName}")
    public ApiReturn<List<AttrDefinitionRsp>> attribute(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                        AttrDefinitionConceptsReq queryReq) {
        return editClient.getAttrDefinitionByConceptIds(kgName, queryReq);
    }

    @ApiOperation("属性定义-批量新增")
    @PostMapping("batchAdd/{kgName}")
    public ApiReturn<List<AttrDefinitionBatchRsp>> batchInsertAttribute(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                                        @RequestBody List<AttrDefinitionReq> attributeList) {
        return editClient.batchAddAttrDefinition(kgName, attributeList);
    }

    @ApiOperation("属性定义-修改")
    @PostMapping("/{kgName}/definition/update")
    public ApiReturn updateAttrDefinition(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                          @RequestBody AttrDefinitionModifyReq modifyReq) {
        return editClient.updateAttrDefinition(kgName, modifyReq);
    }
}
