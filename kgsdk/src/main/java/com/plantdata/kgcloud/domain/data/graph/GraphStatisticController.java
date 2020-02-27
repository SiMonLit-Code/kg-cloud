package com.plantdata.kgcloud.domain.data.graph;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphDataStatisticsInterface;
import com.plantdata.kgcloud.sdk.ComponentStatisticClient;
import com.plantdata.kgcloud.sdk.req.EntityRelationCountReq;
import com.plantdata.kgcloud.sdk.req.EntityRelationStatisticReq;
import com.plantdata.kgcloud.sdk.rsp.EntityRelationStatisticRsp;
import com.plantdata.kgcloud.sdk.rsp.common.BasicValueRsp;
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

    @ApiOperation("图统计")
    @PostMapping("graph/{kgName}")
    public ApiReturn<EntityRelationStatisticRsp> kgStatistic(@PathVariable String kgName,
                                                             @RequestBody @Valid EntityRelationStatisticReq statisticReq) {
        return componentStatisticClient.kgStatistic(kgName, statisticReq);
    }

    @ApiOperation("图数字统计")
    @PostMapping("graph/number/{kgName}")
    public ApiReturn<BasicValueRsp> kgNumberStatistic(@PathVariable String kgName, @RequestBody @Valid EntityRelationCountReq countReq) {
        return componentStatisticClient.kgNumberStatistic(kgName, countReq);
    }


}
