package com.plantdata.kgcloud.domain.data.statistics.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphDataStatisticsInterface;
import com.plantdata.kgcloud.domain.data.obtain.req.CountRelationByEntityReq;
import com.plantdata.kgcloud.domain.data.statistics.req.AttributeStatisticReq;
import com.plantdata.kgcloud.domain.data.statistics.req.StatisticEntityByConceptReq;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/12 18:42
 */
@RestController
@RequestMapping("kgData/entity/statistic")
public class GraphDataEntityStatisticsController implements GraphDataStatisticsInterface {

    @ApiOperation("查询实体的关系度数")
    @PostMapping("degree/{entityId}")
    public ApiReturn<List<?>> countDegree(@ApiParam("实例ID") @PathVariable("entityId") Long entityId,
                                          @RequestBody @Valid CountRelationByEntityReq entityParam,
                                          BindingResult bindingResult) {
        return ApiReturn.success(null);
    }

    @ApiOperation("实例统计，统计实例数量，按概念分组")
    @PostMapping("byConcept/{kgName}")
    public ApiReturn byConcept(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                               @RequestBody @Valid StatisticEntityByConceptReq statisticEntityByConceptReq) {
        return ApiReturn.success(null);
    }

    @ApiOperation("数值属性统计，统计数值属性的数量，按数值属性值分组")
    @PostMapping("byAttrValue/{kgName}")
    public ApiReturn byAttrValue(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                 @RequestBody @Valid AttributeStatisticReq attributeStatisticReq) {
        return ApiReturn.success(null);
    }
}
