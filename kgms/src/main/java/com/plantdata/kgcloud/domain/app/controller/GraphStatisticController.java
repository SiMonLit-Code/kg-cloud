package com.plantdata.kgcloud.domain.app.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.app.controller.module.SdkOpenApiInterface;
import com.plantdata.kgcloud.domain.app.service.KgDataService;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeAttrStatisticByAttrValueReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByConceptIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByEntityIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByAttrIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByConceptReq;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.EdgeStatisticByEntityIdRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/19 13:33
 */
@RestController
@RequestMapping("kgdata/statistic")
public class GraphStatisticController implements SdkOpenApiInterface {
    @Autowired
    private KgDataService kgDataService;

    @ApiOperation("查询实体的关系度数")
    @PostMapping("{kgName}/entity/degree/")
    public ApiReturn<List<EdgeStatisticByEntityIdRsp>> statisticCountEdgeByEntity(@PathVariable("kgName") String kgName,
                                                                                  @RequestBody EdgeStatisticByEntityIdReq statisticReq) {
        return ApiReturn.success(kgDataService.statisticCountEdgeByEntity(kgName, statisticReq));

    }

    @ApiOperation("统计实体根据概念分组")
    @PostMapping("{kgName}/entity/groupByConcept/")
    public ApiReturn<Object> statisticEntityGroupByConcept(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                           @RequestBody EntityStatisticGroupByConceptReq statisticReq) {
        return ApiReturn.success(kgDataService.statEntityGroupByConcept(kgName, statisticReq));
    }

    @ApiOperation("实体属性值统计")
    @PostMapping("{kgName}/attr/groupByAttrValue")
    public ApiReturn<Object> statisticAttrGroupByConcept(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                         @RequestBody EntityStatisticGroupByAttrIdReq statisticReq) {
        return ApiReturn.success(kgDataService.statisticAttrGroupByConcept(kgName, statisticReq));
    }

    @ApiOperation("对象属性统计，统计对象属性的数量，按关系分组")
    @PostMapping("{kgName}/edge/groupByAttrName")
    public ApiReturn<Object> statisticRelation(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                               @RequestBody EdgeStatisticByConceptIdReq statisticReq) {
        return ApiReturn.success(kgDataService.statisticRelation(kgName, statisticReq));
    }

    @ApiOperation("边数值属性统计，按数值属性值分组")
    @PostMapping("{kgName}/edgeAttr/groupByAttrValue")
    public ApiReturn<Object> statEdgeGroupByEdgeValue(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                      EdgeAttrStatisticByAttrValueReq statisticReq) {
        return ApiReturn.success(kgDataService.statEdgeGroupByEdgeValue(kgName, statisticReq));
    }


}
