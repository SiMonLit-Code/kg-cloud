package com.plantdata.kgcloud.domain.kettle.controller;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.domain.dw.req.KettleLogStatisticReq;
import com.plantdata.kgcloud.domain.dw.rsp.GraphMapRsp;
import com.plantdata.kgcloud.domain.dw.rsp.KettleLogStatisticRsp;
import com.plantdata.kgcloud.domain.kettle.service.KettleLogStatisticService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * @author Administrator
 * @Description
 * @data 2020-03-29 9:27
 **/
@Api(tags = "数仓数据统计")
@RestController
@RequestMapping("/kettle/log/statistic")
public class KettleLogStatisticController {

    @Autowired
    private KettleLogStatisticService kettleLogStatisticService;

    @PostMapping("byDate/{id}")
    public ApiReturn<KettleLogStatisticRsp> kettleLogStatisticByDate(
            @ApiParam("数仓id") @PathVariable("id") long dataId,
            @RequestBody KettleLogStatisticReq statisticReq) {
        GraphMapRsp graphMapRsp = new GraphMapRsp();
        graphMapRsp.setDataBaseId(164L);
        graphMapRsp.setTableName("paper");
        ArrayList<GraphMapRsp> graphMapRsps = Lists.newArrayList(graphMapRsp);
        kettleLogStatisticService.fillGraphMapRspCount(graphMapRsps);
        System.out.println(JsonUtils.objToJson(graphMapRsps));
        return ApiReturn.success(kettleLogStatisticService.kettleLogStatisticByDate(dataId,statisticReq));
    }
}
