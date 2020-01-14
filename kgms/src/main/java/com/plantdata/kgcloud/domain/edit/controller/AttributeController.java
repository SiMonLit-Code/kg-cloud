package com.plantdata.kgcloud.domain.edit.controller;

import ai.plantdata.kg.api.edit.BatchApi;
import ai.plantdata.kg.api.edit.resp.BatchRelationVO;
import ai.plantdata.kg.api.edit.resp.BatchResult;
import ai.plantdata.kg.api.edit.resp.UpdateEdgeVO;
import com.plantdata.graph.logging.core.ServiceEnum;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.ValidableList;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.common.converter.RestCopyConverter;
import com.plantdata.kgcloud.domain.edit.aop.EditLogOperation;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.edit.req.attr.AttrConstraintsReq;
import com.plantdata.kgcloud.domain.edit.req.attr.AttrDefinitionAdditionalReq;
import com.plantdata.kgcloud.domain.edit.req.attr.AttrDefinitionSearchReq;
import com.plantdata.kgcloud.domain.edit.req.attr.AttrTemplateReq;
import com.plantdata.kgcloud.domain.edit.req.attr.EdgeAttrDefinitionReq;
import com.plantdata.kgcloud.domain.edit.req.attr.RelationAdditionalReq;
import com.plantdata.kgcloud.domain.edit.req.attr.RelationMetaReq;
import com.plantdata.kgcloud.domain.edit.req.attr.RelationSearchMetaReq;
import com.plantdata.kgcloud.domain.edit.req.attr.RelationSearchReq;
import com.plantdata.kgcloud.domain.edit.req.entity.TripleReq;
import com.plantdata.kgcloud.domain.edit.rsp.AttrConstraintsRsp;
import com.plantdata.kgcloud.domain.edit.rsp.RelationRsp;
import com.plantdata.kgcloud.domain.edit.rsp.TripleRsp;
import com.plantdata.kgcloud.domain.edit.service.AttributeService;
import com.plantdata.kgcloud.sdk.req.EdgeSearchReq;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionBatchRsp;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionModifyReq;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionReq;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionVO;
import com.plantdata.kgcloud.sdk.rsp.OpenBatchResult;
import com.plantdata.kgcloud.sdk.rsp.data.RelationUpdateReq;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionConceptsReq;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.BatchRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.EdgeSearchRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @ApiOperation("属性定义-查询概念的属性定义")
    @GetMapping("/{kgName}")
    public ApiReturn<List<AttrDefinitionRsp>> getAttrDefinitionByConceptId(@PathVariable("kgName") String kgName,
                                                                           @Valid AttrDefinitionSearchReq attrDefinitionSearchReq) {
        return ApiReturn.success(attributeService.getAttrDefinitionByConceptId(kgName, attrDefinitionSearchReq));
    }

    @ApiOperation("属性定义-查询多个概念下的属性定义")
    @PostMapping("/{kgName}/concepts")
    public ApiReturn<List<AttrDefinitionRsp>> getAttrDefinitionByConceptIds(@PathVariable("kgName") String kgName,
                                                                            @RequestBody AttrDefinitionConceptsReq attrDefinitionConceptsReq) {
        return ApiReturn.success(attributeService.getAttrDefinitionByConceptIds(kgName, attrDefinitionConceptsReq));
    }

    @ApiOperation("属性定义-查询实体下的属性定义")
    @GetMapping("/{kgName}/{entityId}")
    public ApiReturn<List<AttrDefinitionRsp>> getAttrDefinitionByEntityId(@PathVariable("kgName") String kgName,
                                                                          @PathVariable("entityId") Long entityId) {
        return ApiReturn.success(attributeService.getAttrDefinitionByEntityId(kgName, entityId));
    }

    @ApiOperation("属性定义-根据属性定义Id查询属性定义")
    @GetMapping("/{kgName}/info/{id}")
    public ApiReturn<AttrDefinitionVO> getAttrDetails(@PathVariable("kgName") String kgName,
                                                      @PathVariable("id") Integer id) {
        return ApiReturn.success(attributeService.getAttrDetails(kgName, id));
    }


    @ApiOperation("属性定义-添加")
    @PostMapping("/{kgName}/definition")
    @EditLogOperation(serviceEnum = ServiceEnum.ATTR_DEFINE)
    public ApiReturn<Integer> addAttrDefinition(@PathVariable("kgName") String kgName,
                                                @Valid @RequestBody AttrDefinitionReq attrDefinitionReq) {
        return ApiReturn.success(attributeService.addAttrDefinition(kgName, attrDefinitionReq));
    }

    @ApiOperation("属性定义-批量添加")
    @PostMapping("/{kgName}/definition/batch")
    @EditLogOperation(serviceEnum = ServiceEnum.ATTR_DEFINE)
    public ApiReturn<OpenBatchResult<AttrDefinitionBatchRsp>> batchAddAttrDefinition(@PathVariable("kgName") String kgName,
                                                                          @Valid @RequestBody ValidableList<AttrDefinitionReq> attrDefinitionReqs) {
        return ApiReturn.success(attributeService.batchAddAttrDefinition(kgName, attrDefinitionReqs));
    }

    @ApiOperation("属性定义-批量修改")
    @PutMapping("/{kgName}/definition/batch")
    @EditLogOperation(serviceEnum = ServiceEnum.ATTR_DEFINE)
    public ApiReturn<OpenBatchResult<AttrDefinitionBatchRsp>> batchModifyAttrDefinition(@PathVariable("kgName") String kgName,
                                                                                        @Valid @RequestBody ValidableList<AttrDefinitionModifyReq> attrDefinitionReqs) {
        return ApiReturn.success(attributeService.batchUpdate(kgName, attrDefinitionReqs));
    }

    @ApiOperation("属性定义-修改")
    @PostMapping("/{kgName}/definition/update")
    @EditLogOperation(serviceEnum = ServiceEnum.ATTR_DEFINE)
    public ApiReturn updateAttrDefinition(@PathVariable("kgName") String kgName,
                                          @Valid @RequestBody AttrDefinitionModifyReq modifyReq) {
        attributeService.updateAttrDefinition(kgName, modifyReq);
        return ApiReturn.success();
    }

    @ApiOperation("属性定义-删除")
    @PostMapping("/{kgName}/definition/delete/{id}")
    @EditLogOperation(serviceEnum = ServiceEnum.ATTR_DEFINE)
    public ApiReturn deleteAttrDefinition(@PathVariable("kgName") String kgName,
                                          @PathVariable("id") Integer id) {
        attributeService.deleteAttrDefinition(kgName, id);
        return ApiReturn.success();
    }

    @ApiOperation("属性定义-边属性定义-添加")
    @PostMapping("/{kgName}/{attrId}/edge/definition")
    @EditLogOperation(serviceEnum = ServiceEnum.ATTR_DEFINE)
    public ApiReturn<Integer> addEdgeAttr(@PathVariable("kgName") String kgName,
                                          @PathVariable("attrId") Integer attrId,
                                          @Valid @RequestBody EdgeAttrDefinitionReq edgeAttrDefinitionReq) {
        return ApiReturn.success(attributeService.addEdgeAttr(kgName, attrId, edgeAttrDefinitionReq));
    }

    @ApiOperation("属性定义-边属性定义-修改")
    @PostMapping("/{kgName}/{attrId}/edge/definition/{seqNo}")
    @EditLogOperation(serviceEnum = ServiceEnum.ATTR_DEFINE)
    public ApiReturn updateEdgeAttr(@PathVariable("kgName") String kgName,
                                    @PathVariable("attrId") Integer attrId,
                                    @PathVariable("seqNo") Integer seqNo,
                                    @Valid @RequestBody EdgeAttrDefinitionReq edgeAttrDefinitionReq) {
        attributeService.updateEdgeAttr(kgName, attrId, seqNo, edgeAttrDefinitionReq);
        return ApiReturn.success();
    }

    @ApiOperation("属性定义-边属性定义-删除")
    @PostMapping("/{kgName}/{attrId}/edge/definition/{seqNo}/delete")
    @EditLogOperation(serviceEnum = ServiceEnum.ATTR_DEFINE)
    public ApiReturn deleteEdgeAttr(@PathVariable("kgName") String kgName,
                                    @PathVariable("attrId") Integer attrId,
                                    @PathVariable("seqNo") Integer seqNo) {
        attributeService.deleteEdgeAttr(kgName, attrId, seqNo);
        return ApiReturn.success();
    }

    @ApiOperation("属性定义-根据属性模板添加")
    @PostMapping("/{kgName}/definition/template")
    @EditLogOperation(serviceEnum = ServiceEnum.SCHEMA_REPO)
    public ApiReturn addAttrDefinitionTemplate(@PathVariable("kgName") String kgName,
                                               @Valid @RequestBody List<AttrTemplateReq> attrTemplateReqs) {
        attributeService.addAttrDefinitionTemplate(kgName, attrTemplateReqs);
        return ApiReturn.success();
    }

    @ApiOperation("关系溯源")
    @PostMapping("/{kgName}/relation")
    public ApiReturn<Page<RelationRsp>> listRelations(@PathVariable("kgName") String kgName,
                                                      RelationSearchReq relationSearchReq,
                                                      @RequestBody RelationSearchMetaReq metaReq) {
        return ApiReturn.success(attributeService.listRelations(kgName, relationSearchReq, metaReq));
    }

    @ApiOperation("关系-批量删除")
    @PostMapping("/{kgName}/batch/relation/delete")
    @EditLogOperation(serviceEnum = ServiceEnum.RELATION_EDIT)
    public ApiReturn deleteRelations(@PathVariable("kgName") String kgName,
                                     @RequestBody List<String> tripleIds) {
        attributeService.deleteRelations(kgName, tripleIds);
        return ApiReturn.success();
    }

    @ApiOperation("关系-根据meta删除")
    @PostMapping("/{kgName}/relation/delete/meta")
    @EditLogOperation(serviceEnum = ServiceEnum.RELATION_TRACE)
    public ApiReturn deleteRelationByMeta(@PathVariable("kgName") String kgName,
                                          @Valid @RequestBody RelationMetaReq relationMetaReq) {
        attributeService.deleteRelationByMeta(kgName, relationMetaReq);
        return ApiReturn.success();
    }

    @ApiOperation("属性约束-查询不满足属性约束列表")
    @PostMapping("/{kgName}/constraints")
    public ApiReturn<List<AttrConstraintsRsp>> listAttrConstraints(@PathVariable("kgName") String kgName,
                                                                   @RequestBody AttrConstraintsReq attrConstraintsReq) {
        return ApiReturn.success(attributeService.listAttrConstraints(kgName, attrConstraintsReq));
    }

    @ApiOperation("属性约束-批量删除不满足属性约束的值")
    @PostMapping("/{kgName}/constraints/delete/{attrId}")
    @EditLogOperation(serviceEnum = ServiceEnum.ATTR_DEFINE)
    public ApiReturn attrConstraintsDelete(@PathVariable("kgName") String kgName,
                                           @PathVariable("attrId") Integer attrId,
                                           @RequestBody List<String> tripleIds) {
        attributeService.attrConstraintsDelete(kgName, attrId, tripleIds);
        return ApiReturn.success();
    }

    @ApiOperation("关系-添加或更新关系的业务信息")
    @PostMapping("/{kgName}/additional/relation")
    public ApiReturn upsertRelationAdditional(@PathVariable("kgName") String kgName,
                                              @Valid @RequestBody RelationAdditionalReq relationAdditionalReq) {
        attributeService.upsertRelationAdditional(kgName, relationAdditionalReq);
        return ApiReturn.success();
    }

    @ApiOperation("属性定义-修改对象属性定义的业务信息")
    @PostMapping("/{kgName}/additional/attr")
    public ApiReturn updateAttrDefinitionAdditional(@PathVariable("kgName") String kgName,
                                                    @Valid @RequestBody AttrDefinitionAdditionalReq additionalReq) {
        attributeService.updateAttrDefinitionAdditional(kgName, additionalReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体-根据属性定义查询实体详情列表")
    @PostMapping("/{kgName}/attr/entity")
    public ApiReturn<List<TripleRsp>> getRelationByAttr(@PathVariable("kgName") String kgName,
                                                        @Valid @RequestBody TripleReq tripleReq) {
        return ApiReturn.success(attributeService.getRelationByAttr(kgName, tripleReq));
    }

    @ApiOperation("关系-批量关系新增")
    @PostMapping("relation/insert/{kgName}")
    @EditLogOperation(serviceEnum = ServiceEnum.SDK)
    public ApiReturn<OpenBatchResult<BatchRelationRsp>> importRelation(@PathVariable("kgName") String kgName,
                                                                       @RequestBody List<BatchRelationRsp> relationList) {
        List<BatchRelationVO> collect = BasicConverter.listConvert(relationList, a -> ConvertUtils.convert(BatchRelationVO.class).apply(a));
        Optional<BatchResult<BatchRelationVO>> resultRestResp = RestRespConverter.convert(batchApi.addRelations(kgName, collect));
        if (!resultRestResp.isPresent()) {
            return ApiReturn.success(OpenBatchResult.empty());
        }
        OpenBatchResult<BatchRelationRsp> relationRsp = RestCopyConverter.copyToBatchResult(resultRestResp.get(), BatchRelationRsp.class);
        return ApiReturn.success(relationRsp);
    }

    @ApiOperation("关系-批量修改关系")
    @PutMapping("relation/update/{kgName}")
    @EditLogOperation(serviceEnum = ServiceEnum.SDK)
    public ApiReturn<List<RelationUpdateReq>> updateRelations(@PathVariable("kgName") String kgName,
                                                              @RequestBody List<RelationUpdateReq> list) {
        List<UpdateEdgeVO> edgeList = RestCopyConverter.copyToNewList(list, UpdateEdgeVO.class);
        Optional<BatchResult<UpdateEdgeVO>> edgeOpt = RestRespConverter.convert(batchApi.updateRelations(kgName,
                edgeList));
        return edgeOpt.map(result -> ApiReturn.success(RestCopyConverter.copyToNewList(result.getSuccess(),
                RelationUpdateReq.class)))
                .orElseGet(() -> ApiReturn.success(Collections.emptyList()));
    }

    @ApiOperation("关系-批量查询关系")
    @PostMapping("relation/search/{kgName}")
    public ApiReturn<List<EdgeSearchRsp>> batchSearchRelation(@PathVariable("kgName") String kgName,
                                                              @RequestBody EdgeSearchReq queryReq) {
        return ApiReturn.success(attributeService.edgeSearch(kgName, queryReq));
    }
}
