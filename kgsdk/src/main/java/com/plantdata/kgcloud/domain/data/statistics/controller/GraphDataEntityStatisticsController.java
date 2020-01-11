package com.plantdata.kgcloud.domain.data.statistics.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphDataStatisticsInterface;
import com.plantdata.kgcloud.sdk.KgDataClient;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByEntityIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByAttrIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByConceptReq;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/12 18:42
 */
@RestController
@RequestMapping("v3/kgdata/entity/statistic")
public class GraphDataEntityStatisticsController implements GraphDataStatisticsInterface {

    @Autowired
    private KgDataClient kgDataClient;

    @ApiOperation("查询实体的关系度数")
    @PostMapping("degree/{kgName}")
    public ApiReturn<List<Map<String, Object>>> countDegree(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                            @RequestBody EdgeStatisticByEntityIdReq entityIdReq) {
        return kgDataClient.statisticCountEdgeByEntity(kgName, entityIdReq);
    }

    @ApiOperation("实例统计，统计实例数量，按概念分组")
    @PostMapping("byConcept/{kgName}")
    public ApiReturn byConcept(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                               @RequestBody EntityStatisticGroupByConceptReq conceptReq) {
        return kgDataClient.statisticEntityGroupByConcept(kgName, conceptReq);
    }

    @ApiOperation("数值属性统计，统计数值属性的数量，按数值属性值分组")
    @PostMapping("byAttrValue/{kgName}")
    public ApiReturn byAttrValue(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                 @RequestBody @Valid EntityStatisticGroupByAttrIdReq attrIdReq) {
        return kgDataClient.statisticAttrGroupByConcept(kgName, attrIdReq);
    }
}
