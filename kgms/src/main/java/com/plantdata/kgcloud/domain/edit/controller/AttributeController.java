package com.plantdata.kgcloud.domain.edit.controller;

import ai.plantdata.kg.api.edit.BatchApi;
import ai.plantdata.kg.api.edit.resp.BatchRelationVO;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.app.converter.RestCopyConverter;
import com.plantdata.kgcloud.domain.edit.req.attr.AttrConstraintsReq;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionConceptsReq;
import com.plantdata.kgcloud.domain.edit.req.attr.AttrDefinitionModifyReq;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionReq;
import com.plantdata.kgcloud.domain.edit.req.attr.AttrDefinitionSearchReq;
import com.plantdata.kgcloud.domain.edit.req.attr.AttrTemplateReq;
import com.plantdata.kgcloud.domain.edit.req.attr.EdgeAttrDefinitionReq;
import com.plantdata.kgcloud.domain.edit.req.attr.RelationMetaReq;
import com.plantdata.kgcloud.domain.edit.req.attr.RelationSearchReq;
import com.plantdata.kgcloud.domain.edit.rsp.AttrConstraintsRsp;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionBatchRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionRsp;
import com.plantdata.kgcloud.domain.edit.rsp.RelationRsp;
import com.plantdata.kgcloud.domain.edit.service.AttributeService;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionVO;
import com.plantdata.kgcloud.sdk.rsp.edit.BatchRelationRsp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/26 15:15
 * @Description:
 */
@Api(tags = "属性定义编辑")
@RestController
@RequestMapping("/edit/attribute")
public class AttributeController {

    @Autowired
    private AttributeService attributeService;
    @Autowired
    private BatchApi batchApi;

    @ApiOperation("查询概念下的属性定义")
    @GetMapping("/{kgName}")
    ApiReturn<List<AttrDefinitionRsp>> getAttrDefinitionByConceptId(@PathVariable("kgName") String kgName,
                                                                    AttrDefinitionSearchReq attrDefinitionSearchReq) {
        return ApiReturn.success(attributeService.getAttrDefinitionByConceptId(kgName, attrDefinitionSearchReq));
    }

    @ApiOperation("查询实体下的属性定义")
    @GetMapping("/{kgName}/{entityId}")
    ApiReturn<List<AttrDefinitionRsp>> getAttrDefinitionByEntityId(@PathVariable("kgName") String kgName,
                                                                   @PathVariable("entityId") Long entityId) {
        return ApiReturn.success(attributeService.getAttrDefinitionByEntityId(kgName, entityId));
    }

    @ApiOperation("属性定义详情")
    @GetMapping("/{kgName}/{id}")
    ApiReturn<AttrDefinitionVO> getAttrDetails(@PathVariable("kgName") String kgName,
                                               @PathVariable("id") Integer id) {
        return ApiReturn.success(attributeService.getAttrDetails(kgName, id));
    }

    @ApiOperation("查询多概念下的属性定义")
    @GetMapping("/{kgName}/concepts")
    ApiReturn<List<AttrDefinitionRsp>> getAttrDefinitionByConceptIds(@PathVariable("kgName") String kgName,
                                                                     AttrDefinitionConceptsReq attrDefinitionConceptsReq) {
        return ApiReturn.success(attributeService.getAttrDefinitionByConceptIds(kgName, attrDefinitionConceptsReq));
    }

    @ApiOperation("添加属性定义")
    @PostMapping("/{kgName}/definition")
    ApiReturn<Integer> addAttrDefinition(@PathVariable("kgName") String kgName,
                                         @Valid @RequestBody AttrDefinitionReq attrDefinitionReq) {
        return ApiReturn.success(attributeService.addAttrDefinition(kgName, attrDefinitionReq));
    }

    @ApiOperation("批量添加属性定义")
    @PostMapping("/{kgName}/definition/batch")
    ApiReturn<List<AttrDefinitionBatchRsp>> batchAddAttrDefinition(@PathVariable("kgName") String kgName,
                                                                   @Valid @RequestBody List<AttrDefinitionReq> attrDefinitionReqs) {
        return ApiReturn.success(attributeService.batchAddAttrDefinition(kgName, attrDefinitionReqs));
    }

    @ApiOperation("修改属性定义")
    @PostMapping("/{kgName}/definition/update")
    ApiReturn updateAttrDefinition(@PathVariable("kgName") String kgName,
                                   @Valid @RequestBody AttrDefinitionModifyReq modifyReq) {
        attributeService.updateAttrDefinition(kgName, modifyReq);
        return ApiReturn.success();
    }

    @ApiOperation("删除属性定义")
    @PostMapping("/{kgName}/definition/delete/{id}")
    ApiReturn deleteAttrDefinition(@PathVariable("kgName") String kgName,
                                   @PathVariable("id") Integer id) {
        attributeService.deleteAttrDefinition(kgName, id);
        return ApiReturn.success();
    }

    @ApiOperation("添加边属性定义")
    @PostMapping("/{kgName}/{attrId}/edge/definition")
    ApiReturn addEdgeAttr(@PathVariable("kgName") String kgName,
                          @PathVariable("attrId") Integer attrId,
                          @Valid @RequestBody EdgeAttrDefinitionReq edgeAttrDefinitionReq) {
        attributeService.addEdgeAttr(kgName, attrId, edgeAttrDefinitionReq);
        return ApiReturn.success();
    }

    @ApiOperation("修改边属性定义")
    @PostMapping("/{kgName}/{attrId}/edge/definition/{seqNo}")
    ApiReturn updateEdgeAttr(@PathVariable("kgName") String kgName,
                             @PathVariable("attrId") Integer attrId,
                             @PathVariable("seqNo") Integer seqNo,
                             @Valid @RequestBody EdgeAttrDefinitionReq edgeAttrDefinitionReq) {
        attributeService.updateEdgeAttr(kgName, attrId, seqNo, edgeAttrDefinitionReq);
        return ApiReturn.success();
    }

    @ApiOperation("删除边属性定义")
    @PostMapping("/{kgName}/{attrId}/edge/definition/{seqNo}/delete")
    ApiReturn deleteEdgeAttr(@PathVariable("kgName") String kgName,
                             @PathVariable("attrId") Integer attrId,
                             @PathVariable("seqNo") Integer seqNo) {
        attributeService.deleteEdgeAttr(kgName, attrId, seqNo);
        return ApiReturn.success();
    }

    @ApiOperation("根据属性模板添加属性定义")
    @PostMapping("/{kgName}/{attrId}/definition/template")
    ApiReturn addAttrDefinitionTemplate(@PathVariable("kgName") String kgName,
                                        @Valid @RequestBody List<AttrTemplateReq> attrTemplateReqs) {
        attributeService.addAttrDefinitionTemplate(kgName, attrTemplateReqs);
        return ApiReturn.success();
    }

    @ApiOperation("关系溯源")
    @PostMapping("/{kgName}/relation")
    ApiReturn<Page<RelationRsp>> listRelations(@PathVariable("kgName") String kgName,
                                               @Valid @RequestBody RelationSearchReq relationSearchReq) {
        return ApiReturn.success(attributeService.listRelations(kgName, relationSearchReq));
    }

    @ApiOperation("批量删除关系")
    @PostMapping("/{kgName}/batch/relation/delete")
    ApiReturn deleteRelations(@PathVariable("kgName") String kgName,
                              @RequestBody List<String> tripleIds) {
        attributeService.deleteRelations(kgName, tripleIds);
        return ApiReturn.success();
    }

    @ApiOperation("根据meta删除关系")
    @PostMapping("/{kgName}/relation/delete/meta")
    ApiReturn deleteRelationByMeta(@PathVariable("kgName") String kgName,
                                   @Valid @RequestBody RelationMetaReq relationMetaReq) {
        attributeService.deleteRelationByMeta(kgName, relationMetaReq);
        return ApiReturn.success();
    }

    @ApiOperation("不满足属性约束列表")
    @GetMapping("/{kgName}/constraints")
    ApiReturn<List<AttrConstraintsRsp>> listAttrConstraints(@PathVariable("kgName") String kgName,
                                                            AttrConstraintsReq attrConstraintsReq) {
        return ApiReturn.success(attributeService.listAttrConstraints(kgName, attrConstraintsReq));
    }

    @ApiOperation("批量删除不满足属性约束的值")
    @PostMapping("/{kgName}/constraints/delete/{attrId}")
    ApiReturn attrConstraintsDelete(@PathVariable("kgName") String kgName,
                                    @PathVariable("attrId") Integer attrId,
                                    List<String> tripleIds) {
        attributeService.attrConstraintsDelete(kgName, attrId, tripleIds);
        return ApiReturn.success();
    }


    @ApiOperation("批量关系新增")
    @PostMapping("relation/insert/{kgName}")
    public ApiReturn<BatchRelationRsp> importRelation(@PathVariable String kgName,
                                                      @RequestBody BatchRelationRsp relation) {
        BatchRelationVO batchRelationVO = new BatchRelationVO();
        BeanUtils.copyProperties(relation, batchRelationVO);
        BatchRelationRsp relationRsp = RestCopyConverter.copyRestRespResult(batchApi.addRelations(kgName, Lists.newArrayList(batchRelationVO)), new BatchRelationRsp());
        return ApiReturn.success(relationRsp);
    }

}
