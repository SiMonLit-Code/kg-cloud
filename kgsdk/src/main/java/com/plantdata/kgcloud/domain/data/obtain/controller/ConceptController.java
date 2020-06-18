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

    @ApiOperation(value = "概念树获取", notes = "获取知识图谱的概念树")
    @GetMapping("{kgName}")
    public ApiReturn<List<BasicInfoVO>> conceptTree(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                    @ApiParam("概念id 和key不能同时为null") @RequestParam(value = "conceptId", required = false) Long conceptId,
                                                    @ApiParam("概念唯一标识") @RequestParam(value = "conceptKey", required = false) String conceptKey) {
        return appClient.conceptTree(kgName, conceptId, conceptKey);
    }

    @ApiOperation(value = "添加概念", notes = "添加概念，需要制定所属父概念id。")
    @PostMapping("{kgName}")
    public ApiReturn<Long> addConcept(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                      @RequestBody ConceptAddReq basicInfoReq) {
        return kgDataClient.createConcept(kgName, basicInfoReq);
    }

    @ApiOperation(value = "更新概念", notes = "修改概念信息。")
    @PutMapping("/{kgName}")
    public ApiReturn updateConcept(@PathVariable("kgName") String kgName,
                                   @RequestBody BasicInfoModifyReq basicInfoModifyReq) {
        return editClient.updateConcept(kgName, basicInfoModifyReq);
    }

    @ApiOperation(value = "删除概念", notes = "修改概念信息。")
    @DeleteMapping("{kgName}/{conceptId}")
    public ApiReturn deleteConcept(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                   @ApiParam(value = "概念id", required = true) @PathVariable("conceptId") Long conceptId) {
        return editClient.deleteConcept(kgName, conceptId, false);
    }
}
