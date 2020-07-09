package com.plantdata.kgcloud.domain.edit.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import com.plantdata.graph.logging.core.ServiceEnum;
import com.plantdata.kgcloud.domain.edit.aop.EditLogOperation;
import com.plantdata.kgcloud.domain.edit.req.basic.AdditionalModifyReq;
import com.plantdata.kgcloud.domain.edit.req.basic.ConceptReplaceReq;
import com.plantdata.kgcloud.domain.edit.req.basic.GisModifyReq;
import com.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import com.plantdata.kgcloud.domain.edit.service.ConceptService;
import com.plantdata.kgcloud.sdk.req.edit.BasicInfoModifyReq;
import com.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/16 18:31
 * @Description:
 */
@Api(tags = "概念编辑")
@RestController
@RequestMapping("/edit/concept")
public class ConceptController {

    @Autowired
    private ConceptService conceptService;

    @Autowired
    private BasicInfoService basicInfoService;

    @ApiOperation("修改概念名称,消歧,key")
    @PostMapping("/{kgName}/update")
    @EditLogOperation(serviceEnum = ServiceEnum.CONCEPT_DEFINE)
    public ApiReturn updateConcept(@PathVariable("kgName") String kgName,
                                   @Valid @RequestBody BasicInfoModifyReq basicInfoModifyReq) {
        basicInfoService.updateBasicInfo(kgName, basicInfoModifyReq);
        return ApiReturn.success();
    }

    @ApiOperation("修改父概念")
    @PostMapping("/{kgName}/update/concept")
    @EditLogOperation(serviceEnum = ServiceEnum.CONCEPT_DEFINE)
    public ApiReturn updateConcept(@PathVariable("kgName") String kgName,
                                   @Valid @RequestBody ConceptReplaceReq conceptReplaceReq) {
        conceptService.replaceConceptId(kgName, conceptReplaceReq);
        return ApiReturn.success();
    }

    @ApiOperation("获取概念树")
    @PostMapping("/{kgName}/{conceptId}/tree")
    public ApiReturn<List<BasicInfoVO>> getConceptTree(@PathVariable("kgName") String kgName,
                                                       @PathVariable("conceptId") Long conceptId) {
        return ApiReturn.success(conceptService.getConceptTree(kgName, conceptId));
    }

    @ApiOperation("修改概念gis")
    @PostMapping("/{kgName}/update/gis")
    @EditLogOperation(serviceEnum = ServiceEnum.CONCEPT_DEFINE)
    public ApiReturn updateGis(@PathVariable("kgName") String kgName, @Valid @RequestBody GisModifyReq gisModifyReq) {
        conceptService.updateGis(kgName, gisModifyReq);
        return ApiReturn.success();
    }

    @ApiOperation("修改概念额外信息")
    @PostMapping("/{kgName}/update/additional")
    @EditLogOperation(serviceEnum = ServiceEnum.CONCEPT_DEFINE)
    public ApiReturn updateAdditional(@PathVariable("kgName") String kgName,
                                      @Valid @RequestBody AdditionalModifyReq additionalModifyReq) {
        conceptService.updateAdditional(kgName, additionalModifyReq);
        return ApiReturn.success();
    }


}
