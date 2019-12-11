package com.plantdata.kgcloud.domain.data.obtain.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphDataObtainInterface;
import com.plantdata.kgcloud.domain.data.obtain.req.StatisticConfigReq;
import com.plantdata.kgcloud.domain.data.obtain.rsp.StatisticConfigRsp;
import com.plantdata.kgcloud.sdk.KgmsClient;
import com.plantdata.kgcloud.sdk.req.GraphConfStatisticalReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfStatisticalRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/15 16:01
 */
@RestController
@RequestMapping("v3/kgData/statisticConfig")
public class GraphStatisticConfigController implements GraphDataObtainInterface {

    @Autowired
    private KgmsClient kgmsClient;

    @ApiOperation("获取统计配置列表")
    @GetMapping("{kgName}")
    public ApiReturn<List<GraphConfStatisticalRsp>> configList(@ApiParam("图谱名称") @PathVariable("kgName") String kgName) {

        return kgmsClient.selectStatistical(kgName);
    }


    @ApiOperation("批量新增统计配置")
    @PostMapping("batch/{kgName}")
    public ApiReturn<List<StatisticConfigRsp>> batchAdd(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                        @RequestBody List<StatisticConfigReq> configList) {
        //todo kgms
        return ApiReturn.success();
    }

    @ApiOperation("批量修改统计配置")
    @PatchMapping("batch/{kgName}")
    public ApiReturn<List<StatisticConfigRsp>> batchModify(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                           @RequestBody List<StatisticConfigReq> configList) {
        //todo kgms
        return ApiReturn.success();
    }

    @ApiOperation("批量删除统计配置")
    @DeleteMapping("batch/{kgName}")
    public ApiReturn batchRemove(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                 @RequestParam("ids") List<Integer> ids) {
        //todo kgms
        return ApiReturn.success();
    }

    @ApiOperation("新增统计配置")
    @PostMapping("{kgName}")
    public ApiReturn<GraphConfStatisticalRsp> add(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                  @RequestBody GraphConfStatisticalReq config) {
        return kgmsClient.saveStatistical(kgName, config);
    }

    @ApiOperation("修改统计配置")
    @PatchMapping("{id}")
    public ApiReturn<GraphConfStatisticalRsp> modify(@ApiParam("配置id") @PathVariable("id") Long id,
                                                     @RequestBody GraphConfStatisticalReq config) {
        return kgmsClient.updateStatistical(id, config);
    }

    @ApiOperation("删除统计配置")
    @DeleteMapping("{id}")
    public ApiReturn remove(@ApiParam("配置id") @PathVariable("id") Long id) {
        return kgmsClient.deleteStatistical(id);
    }

}
