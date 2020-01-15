package com.plantdata.kgcloud.domain.data.obtain.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphDataObtainInterface;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.EditClient;
import com.plantdata.kgcloud.sdk.KgDataClient;
import com.plantdata.kgcloud.sdk.req.edit.BasicInfoModifyReq;
import com.plantdata.kgcloud.sdk.req.edit.ConceptAddReq;
import com.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/12 18:32
 */
@RestController
@RequestMapping("v3/kgdata/concept")
public class ConceptController implements GraphDataObtainInterface {

    @Resource
    private AppClient appClient;
    @Resource
    private EditClient editClient;
    @Resource
    private KgDataClient kgDataClient;

    @ApiOperation("概念树获取")
    @GetMapping("{kgName}")
    public ApiReturn<List<BasicInfoVO>> conceptTree(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                    @ApiParam("概念id 和key不能同时为null") @RequestParam(value = "conceptId", required = false) Long conceptId,
                                                    @ApiParam("概念唯一标识") @RequestParam(value = "conceptKey", required = false) String conceptKey) {
        return appClient.conceptTree(kgName, conceptId, conceptKey);
    }

    @ApiOperation("概念-新增")
    @PostMapping("{kgName}")
    public ApiReturn<Long> addConcept(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                      @RequestBody ConceptAddReq basicInfoReq) {
        return kgDataClient.createConcept(kgName, basicInfoReq);
    }

    @ApiOperation("概念-修改")
    @PutMapping("/{kgName}")
    public ApiReturn updateConcept(@PathVariable("kgName") String kgName,
                                   @RequestBody BasicInfoModifyReq basicInfoModifyReq) {
        return editClient.updateConcept(kgName, basicInfoModifyReq);
    }

    @ApiOperation("概念-删除")
    @DeleteMapping("{kgName}/{conceptId}")
    public ApiReturn deleteConcept(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                   @ApiParam(value = "概念id", required = true) @PathVariable("conceptId") Long conceptId) {
        return editClient.deleteConcept(kgName, conceptId);
    }
}
