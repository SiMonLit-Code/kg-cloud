package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.req.EdgeSearchReq;
import com.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionBatchRsp;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionModifyReq;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionReq;
import com.plantdata.kgcloud.sdk.req.edit.BasicInfoModifyReq;
import com.plantdata.kgcloud.sdk.req.edit.BasicInfoReq;
import com.plantdata.kgcloud.sdk.rsp.OpenBatchResult;
import com.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.data.RelationUpdateReq;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionConceptsReq;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.BatchRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.DeleteResult;
import com.plantdata.kgcloud.sdk.rsp.edit.EdgeSearchRsp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/5 12:21
 */
@FeignClient(value = "kgms", path = "/edit", contextId = "basicInfoClient")
public interface EditClient {

    /**
     * 添加概念或实体
     *
     * @param kgName       ..
     * @param basicInfoReq ..
     * @return .
     */
    @PostMapping("basic/{kgName}")
    ApiReturn<Long> createConcept(@PathVariable("kgName") String kgName,
                                  @RequestBody BasicInfoReq basicInfoReq);

    /**
     * 删除概念
     *
     * @param kgName 图谱名称
     * @param id     id
     * @return 。
     */
    @DeleteMapping("basic/{kgName}/{id}")
    ApiReturn deleteConcept(@PathVariable("kgName") String kgName, @PathVariable("id") Long id);

    /**
     * 删除属性定义
     *
     * @param kgName 图谱名称
     * @param id     id
     * @return .
     */
    @PostMapping("attribute/{kgName}/definition/delete/{id}")
    ApiReturn deleteAttrDefinition(@PathVariable("kgName") String kgName,
                                   @PathVariable("id") Integer id);

    /**
     * 批量新增属性定义
     *
     * @param kgName             图谱名称
     * @param attrDefinitionReqs 批量参数
     * @return .
     */
    @PostMapping("attribute/{kgName}/definition/batch")
    ApiReturn<List<AttrDefinitionBatchRsp>> batchAddAttrDefinition(@PathVariable("kgName") String kgName,
                                                                   @RequestBody List<AttrDefinitionReq> attrDefinitionReqs);

    /**
     * 查询多概念下的属性定义
     *
     * @param kgName                    图谱名称
     * @param attrDefinitionConceptsReq 批量参数
     * @return .
     */
    @GetMapping("attribute/{kgName}/concepts")
    ApiReturn<List<AttrDefinitionRsp>> getAttrDefinitionByConceptIds(@PathVariable("kgName") String kgName,
                                                                     AttrDefinitionConceptsReq attrDefinitionConceptsReq);

    /**
     * 批量新增关系
     *
     * @param kgName       图谱名称
     * @param relationList 批量参数
     * @return 。
     */
    @PostMapping("attribute/relation/insert/{kgName}")
    ApiReturn<OpenBatchResult<BatchRelationRsp>> importRelation(@PathVariable("kgName") String kgName,
                                                                @RequestBody List<BatchRelationRsp> relationList);

    /**
     * 修改概念名称,消歧,key
     *
     * @param kgName             图谱名称
     * @param basicInfoModifyReq 批量参数
     * @return .
     */
    @PostMapping("concept/{kgName}/update")
    ApiReturn updateConcept(@PathVariable("kgName") String kgName,
                            @RequestBody BasicInfoModifyReq basicInfoModifyReq);

    /**
     * 修改属性定义
     *
     * @param kgName    图谱名称
     * @param modifyReq 修改参数
     * @return 。
     */
    @PostMapping("attribute/{kgName}/definition/update")
    ApiReturn updateAttrDefinition(@PathVariable("kgName") String kgName,
                                   @RequestBody AttrDefinitionModifyReq modifyReq);

    /**
     * 批量修改属性定义
     *
     * @param kgName             图谱名称
     * @param attrDefinitionReqs req
     * @return .
     */
    @PatchMapping("attribute/{kgName}/definition/batch")
    ApiReturn<OpenBatchResult<AttrDefinitionBatchRsp>> batchModifyAttrDefinition(@PathVariable("kgName") String kgName,
                                                                                 @RequestBody List<AttrDefinitionReq> attrDefinitionReqs);

    /**
     * 批量删除实体
     *
     * @param kgName 图谱名称
     * @param ids    实体id
     * @return 。。
     */
    @PostMapping("entity/{kgName}/batch/delete")
    ApiReturn<List<DeleteResult>> batchDeleteEntities(@PathVariable("kgName") String kgName,
                                                      @RequestBody List<Long> ids);


    /**
     * 批量删除关系
     *
     * @param kgName    图谱名称
     * @param tripleIds 关系id
     * @return 。
     */
    @PostMapping("attribute/{kgName}/batch/relation/delete")
    ApiReturn deleteRelations(@PathVariable("kgName") String kgName,
                              @RequestBody List<String> tripleIds);

    /**
     * 批量修改关系
     *
     * @param kgName kgName
     * @param list   req
     * @return .
     */
    @PatchMapping("attribute/relation/update/{kgName}")
    ApiReturn<List<RelationUpdateReq>> updateRelations(@PathVariable("kgName") String kgName, @RequestBody List<RelationUpdateReq> list);


    /**
     * 实体查询
     *
     * @param kgName
     * @param conceptId
     * @param conceptKey
     * @param query
     * @param page
     * @param size
     * @return
     */
    @GetMapping("entity/{kgName}/list/search")
    ApiReturn<List<OpenEntityRsp>> queryEntityList(@PathVariable("kgName") String kgName,
                                                   @RequestParam(value = "conceptId", required = false) Long conceptId,
                                                   @RequestParam(value = "conceptKey", required = false) String conceptKey,
                                                   @RequestParam(value = "query", required = false) String query,
                                                   @RequestParam(value = "page", required = false) Integer page,
                                                   @RequestParam(value = "size", required = false) Integer size);

    /**
     * 批量新增实体
     *
     * @param kgName      kgName
     * @param add         req
     * @param batchEntity 。
     * @return 。
     */
    @PostMapping("entity/{kgName}")
    ApiReturn<OpenBatchResult<OpenBatchSaveEntityRsp>> saveOrUpdate(@PathVariable("kgName") String kgName, @RequestParam(value = "add", required = false) boolean add,
                                                                    @RequestBody List<OpenBatchSaveEntityRsp> batchEntity);

    /**
     * 批量查询关系
     *
     * @param kgName   kgName
     * @param queryReq req
     * @return ..
     */
    @PostMapping("attribute/relation/search/{kgName}")
    ApiReturn<List<EdgeSearchRsp>> batchSearchRelation(@PathVariable("kgName") String kgName, @RequestBody EdgeSearchReq queryReq);

}
