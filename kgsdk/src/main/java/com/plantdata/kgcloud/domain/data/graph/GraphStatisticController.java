package com.plantdata.kgcloud.domain.data.graph;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphDataStatisticsInterface;
import com.plantdata.kgcloud.sdk.ComponentStatisticClient;
import com.plantdata.kgcloud.sdk.req.EntityRelationCountReq;
import com.plantdata.kgcloud.sdk.req.EntityRelationStatisticReq;
import com.plantdata.kgcloud.sdk.rsp.common.BasicValueRsp;
import com.plantdata.kgcloud.sdk.rsp.common.KgStatisticRsp;
import com.plantdata.kgcloud.sdk.rsp.common.MultiMeasureStatisticResultRsp;
import com.plantdata.kgcloud.sdk.rsp.common.StatisticResultRsp;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Administrator
 * @Description
 * @data 2020-02-17 14:23
 **/
@RestController
@RequestMapping("v3/kgdata/graph/statistic")
public class GraphStatisticController implements GraphDataStatisticsInterface {

    @Autowired
    private ComponentStatisticClient componentStatisticClient;

    @ApiOperation(value = "图统计", notes = "指定维度，目标，度量，筛选条件的类型和方式 得到图谱统计结果（二维）")
    @PostMapping("{kgName}")
    public ApiReturn<KgStatisticRsp<StatisticResultRsp>> kgStatistic(@PathVariable String kgName,
                                                                     @RequestBody @Valid EntityRelationStatisticReq statisticReq) {
        return componentStatisticClient.kgStatistic(kgName, statisticReq);
    }

    @ApiOperation(value = "图数字统计", notes = "指定维度，目标，度量，筛选条件的类型和方式 进行计数和求和 得到图谱统计结果")
    @PostMapping("number/{kgName}")
    public ApiReturn<BasicValueRsp> kgNumberStatistic(@PathVariable String kgName, @RequestBody @Valid EntityRelationCountReq countReq) {
        return componentStatisticClient.kgNumberStatistic(kgName, countReq);
    }

    @ApiOperation(value = "KGQL图统计", notes = "按照kgql语句规则 进行计数和求和 得到图谱统计结果")
    @PostMapping({"kgql/{kgName}"})
    public ApiReturn<KgStatisticRsp<MultiMeasureStatisticResultRsp>> kgKGQLStatistic(@PathVariable String kgName, @RequestBody String kgQl) {
        return componentStatisticClient.kgKGQLStatistic(kgName,kgQl);
    }


}
