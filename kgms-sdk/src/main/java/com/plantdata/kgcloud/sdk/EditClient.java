package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionBatchRsp;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionReq;
import com.plantdata.kgcloud.sdk.req.edit.BasicInfoReq;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionConceptsReq;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionRsp;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
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
     * @return。
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
                                                                   @Valid @RequestBody List<AttrDefinitionReq> attrDefinitionReqs);

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
}
