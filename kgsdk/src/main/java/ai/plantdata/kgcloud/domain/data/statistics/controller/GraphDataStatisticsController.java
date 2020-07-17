package ai.plantdata.kgcloud.domain.data.statistics.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.domain.common.module.GraphDataStatisticsInterface;
import ai.plantdata.kgcloud.sdk.KgDataClient;
import ai.plantdata.kgcloud.sdk.req.app.statistic.EdgeAttrStatisticByAttrValueReq;
import ai.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByConceptIdReq;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/1 15:37
 */
@Slf4j
@RestController
@RequestMapping("v3/kgData/relation/statistic/")
public class GraphDataStatisticsController implements GraphDataStatisticsInterface {

    @Autowired
    private KgDataClient kgDataClient;

    @ApiOperation(value ="实体按属性类型统计",notes = "统计知识图谱不同类型属性（对象属性）的分布。")
    @PostMapping("{kgName}/attrValue")
    public ApiReturn<Object> relationCountByAttrValue(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                      @RequestBody EdgeStatisticByConceptIdReq conceptIdReq) {
        return kgDataClient.statisticRelation(kgName, conceptIdReq);
    }

    @ApiOperation(value = "边关系按属性值统计",notes = "统计知识图谱关系上的一种属性，值的分布情况。")
    @PostMapping("{kgName}/edge/attrValue/")
    public ApiReturn<Object> statEdgeGroupByEdgeValue(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                      @RequestBody EdgeAttrStatisticByAttrValueReq conceptIdReq) {
        return kgDataClient.statEdgeGroupByEdgeValue(kgName, conceptIdReq);
    }

}
