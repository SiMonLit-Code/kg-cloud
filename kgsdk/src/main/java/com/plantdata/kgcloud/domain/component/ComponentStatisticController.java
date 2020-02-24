package com.plantdata.kgcloud.domain.component;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.ComponentStatisticClient;
import com.plantdata.kgcloud.sdk.req.DataSetCountReq;
import com.plantdata.kgcloud.sdk.req.DataSetStatisticReq;
import com.plantdata.kgcloud.sdk.req.EntityRelationCountReq;
import com.plantdata.kgcloud.sdk.req.EntityRelationStatisticReq;
import com.plantdata.kgcloud.sdk.rsp.DataSetStatisticRsp;
import com.plantdata.kgcloud.sdk.rsp.EntityRelationStatisticRsp;
import com.plantdata.kgcloud.sdk.rsp.common.BasicValueRsp;
import io.swagger.annotations.Api;
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
@RequestMapping("component/statistic")
@Api(tags = "编排统计接口")
public class ComponentStatisticController {

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

    @ApiOperation("数据集统计")
    @PostMapping("dataSet/{dataSetId}")
    public ApiReturn<DataSetStatisticRsp> dataSetStatistic(@PathVariable Long dataSetId, @RequestBody @Valid DataSetStatisticReq statisticReq) {
        return componentStatisticClient.dataSetStatistic(dataSetId, statisticReq);
    }

    @ApiOperation("数据集数字统计")
    @PostMapping("dataSet/number/{dataSetId}")
    public ApiReturn<BasicValueRsp> dataSetNumberStatistic(@PathVariable Long dataSetId, @RequestBody @Valid DataSetCountReq countReq) {
        return componentStatisticClient.dataSetNumberStatistic(dataSetId, countReq);
    }


}
