package ai.plantdata.kgcloud.domain.data.obtain.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.domain.common.module.GraphDataObtainInterface;
import ai.plantdata.kgcloud.sdk.EditClient;
import ai.plantdata.kgcloud.sdk.KgDataClient;
import ai.plantdata.kgcloud.sdk.req.edit.AttrDefinitionBatchRsp;
import ai.plantdata.kgcloud.sdk.req.edit.AttrDefinitionModifyReq;
import ai.plantdata.kgcloud.sdk.req.edit.AttrDefinitionReq;
import ai.plantdata.kgcloud.sdk.rsp.OpenBatchResult;
import ai.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation(value = "获取属性定义",notes = "获取属性定义")
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

    @ApiOperation(value = "修改属性定义",notes = "修改属性定义")
    @PutMapping("{kgName}")
    public ApiReturn updateAttrDefinition(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                          @RequestBody AttrDefinitionModifyReq modifyReq) {
        return editClient.updateAttrDefinition(kgName, modifyReq);
    }

    @ApiOperation(value = "批量添加属性定义", notes = "批量添加属性定义")
    @PostMapping("{kgName}/batch")
    public ApiReturn<OpenBatchResult<AttrDefinitionBatchRsp>> batchInsertAttribute(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                                                   @RequestBody List<AttrDefinitionReq> attributeList) {
        return editClient.batchAddAttrDefinition(kgName, attributeList);
    }

    @ApiOperation(value = "批量修改属性定义", notes = "批量修改属性定义")
    @PutMapping("{kgName}/batch")
    public ApiReturn<OpenBatchResult<AttrDefinitionBatchRsp>> batchModify(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                                          @RequestBody List<AttrDefinitionModifyReq> attributeList) {
        return editClient.batchModifyAttrDefinition(kgName, attributeList);
    }

    @ApiOperation(value = "删除属性定义",notes = "删除属性定义")
    @DeleteMapping("{kgName}/{id}")
    public ApiReturn delete(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                            @ApiParam(value = "属性id", required = true) @PathVariable("id") Integer id) {
        return editClient.deleteAttrDefinition(kgName, id);
    }

}
