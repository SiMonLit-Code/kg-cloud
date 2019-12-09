package com.plantdata.kgcloud.domain.structure.analysis.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphStructureAnalysisInterface;
import com.plantdata.kgcloud.sdk.rsp.app.CoordinatesRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/14 15:33
 */
@RestController
@RequestMapping("algorithm")
public class GraphAlgorithmAnalysisController implements GraphStructureAnalysisInterface {

    @ApiOperation("复杂图分析坐标显示")
    @GetMapping("coordinates/{kgName}/{azkId}")
    public ApiReturn<CoordinatesRsp> coordinates(@ApiParam("图谱名称") @PathVariable("kgName") String kgName, @ApiParam("任务id") @PathVariable("azkId") int azkId,
                                                 @ApiParam("类型") @RequestParam(value = "type", defaultValue = "louvain") String type,
                                                 @ApiParam("显示数量") @RequestParam(value = "size", defaultValue = "100") int size) {
        return ApiReturn.success(null);
    }
}
