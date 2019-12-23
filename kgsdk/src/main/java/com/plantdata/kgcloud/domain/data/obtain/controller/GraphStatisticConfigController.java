package com.plantdata.kgcloud.domain.data.obtain.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphDataObtainInterface;
import com.plantdata.kgcloud.sdk.KgmsClient;
import com.plantdata.kgcloud.sdk.req.GraphConfStatisticalReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfStatisticalRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/15 16:01
 */
@RestController
@RequestMapping("v3/kgdata/statisticConfig")
public class GraphStatisticConfigController implements GraphDataObtainInterface {

    @Autowired
    private KgmsClient kgmsClient;

    @ApiOperation("统计配置-获取列表")
    @GetMapping("{kgName}")
    public ApiReturn<List<GraphConfStatisticalRsp>> configList(@ApiParam("图谱名称") @PathVariable("kgName") String kgName) {

        return kgmsClient.selectStatistical(kgName);
    }


    @ApiOperation("统计配置-批量新增")
    @PostMapping("batch")
    public ApiReturn<List<GraphConfStatisticalRsp>> batchAdd(@RequestBody List<GraphConfStatisticalReq> listReq) {

        return kgmsClient.saveStatisticalBatch(listReq);
    }

    @ApiOperation("统计配置-批量修改")
    @PutMapping("batch")
    public ApiReturn<List<GraphConfStatisticalRsp>> batchModify(@RequestBody List<GraphConfStatisticalReq> reqList) {

        return kgmsClient.updateStatisticalBatch(reqList);
    }

    @ApiOperation("统计配置-批量删除")
    @DeleteMapping("batch")
    public ApiReturn batchRemove(@RequestParam("ids") List<Long> ids) {
        ids.removeIf(Objects::isNull);
        return kgmsClient.deleteStatisticalBatch(ids);
    }

    @ApiOperation("统计配置-新增")
    @PostMapping("{kgName}")
    public ApiReturn<GraphConfStatisticalRsp> add(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                  @RequestBody GraphConfStatisticalReq config) {
        return kgmsClient.saveStatistical(kgName, config);
    }

    @ApiOperation("统计配置-修改")
    @PutMapping("{id}")
    public ApiReturn<GraphConfStatisticalRsp> modify(@ApiParam("配置id") @PathVariable("id") Long id,
                                                     @RequestBody GraphConfStatisticalReq config) {
        return kgmsClient.updateStatistical(id, config);
    }

    @ApiOperation("统计配置-删除")
    @DeleteMapping("{id}")
    public ApiReturn remove(@ApiParam("配置id") @PathVariable("id") Long id) {
        return kgmsClient.deleteStatistical(id);
    }

}
