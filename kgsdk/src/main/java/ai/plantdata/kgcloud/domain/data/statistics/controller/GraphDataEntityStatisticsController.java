package ai.plantdata.kgcloud.domain.data.statistics.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.domain.common.module.GraphDataStatisticsInterface;
import ai.plantdata.kgcloud.sdk.KgDataClient;
import ai.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByEntityIdReq;
import ai.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByAttrIdReq;
import ai.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByConceptReq;
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

    @ApiOperation(value = "实体关系度数统计",notes = "查询实体的关系度数（出度、入度、度），支持分层查询。")
    @PostMapping("degree/{kgName}")
    public ApiReturn<List<Map<String, Object>>> countDegree(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                            @RequestBody EdgeStatisticByEntityIdReq entityIdReq) {
        return kgDataClient.statisticCountEdgeByEntity(kgName, entityIdReq);
    }

    @ApiOperation(value = "实体按概念统计",notes = "统计知识图谱每个概念下的直接实例数量。")
    @PostMapping("concept/{kgName}")
    public ApiReturn byConcept(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                               @RequestBody EntityStatisticGroupByConceptReq conceptReq) {
        return kgDataClient.statisticEntityGroupByConcept(kgName, conceptReq);
    }

    @ApiOperation(value = "实体按属性类型统计",notes = "统计知识图谱不同类型属性（对象属性）的分布。")
    @PostMapping("attrValue/{kgName}")
    public ApiReturn byAttrValue(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                 @RequestBody @Valid EntityStatisticGroupByAttrIdReq attrIdReq) {
        return kgDataClient.statisticAttrGroupByConcept(kgName, attrIdReq);
    }
}
