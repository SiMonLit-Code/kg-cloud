package com.plantdata.kgcloud.domain.data.obtain.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphDataObtainInterface;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.EditClient;
import com.plantdata.kgcloud.sdk.req.edit.BasicInfoModifyReq;
import com.plantdata.kgcloud.sdk.req.edit.BasicInfoReq;
import com.plantdata.kgcloud.sdk.rsp.app.main.BasicConceptRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/12 18:32
 */
@RestController
@RequestMapping("kgData/concept")
public class ConceptController implements GraphDataObtainInterface {

    @Resource
    private AppClient appClient;
    @Resource
    private EditClient editClient;

    @ApiOperation("获取概念树")
    @GetMapping("{kgName}")
    public ApiReturn<List<BasicConceptRsp>> conceptTree(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                        @ApiParam("概念id") @RequestParam("conceptId") Long conceptId,
                                                        @ApiParam("概念唯一标识") @RequestParam("conceptKey") String conceptKey) {
        return appClient.conceptTree(kgName, conceptId, conceptKey);
    }

    @ApiOperation("添加概念")
    @PostMapping("add/{kgName}")
    public ApiReturn<Long> addConcept(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                      @RequestBody BasicInfoReq basicInfoReq) {
        return editClient.createConcept(kgName, basicInfoReq);
    }

    @ApiOperation("删除概念")
    @DeleteMapping("{kgName}/{conceptId}")
    public ApiReturn deleteConcept(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                   @ApiParam(value = "概念id", required = true) @PathVariable("conceptId") Long conceptId) {
        return editClient.deleteConcept(kgName, conceptId);
    }

    @ApiOperation("修改概念")
    @PostMapping("/{kgName}/update")
    public ApiReturn updateConcept(@PathVariable("kgName") String kgName,
                                   @RequestBody BasicInfoModifyReq basicInfoModifyReq) {
        return editClient.updateConcept(kgName, basicInfoModifyReq);
    }

}
