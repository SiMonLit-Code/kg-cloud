package com.plantdata.kgcloud.domain.kettle.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.dw.req.KettleLogStatisticReq;
import com.plantdata.kgcloud.domain.dw.rsp.KettleLogStatisticRsp;
import com.plantdata.kgcloud.domain.kettle.service.KettleLogStatisticService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
            @RequestBody @Valid KettleLogStatisticReq statisticReq) {
        return ApiReturn.success(kettleLogStatisticService.kettleLogStatisticByDate(dataId,statisticReq));
    }
}
