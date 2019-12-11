package com.plantdata.kgcloud.domain.graph.config.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfStatistical;
import com.plantdata.kgcloud.sdk.req.GraphConfFocusReq;
import com.plantdata.kgcloud.sdk.req.GraphConfStatisticalReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfFocusRsp;
import com.plantdata.kgcloud.sdk.rsp.GraphConfStatisticalRsp;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfStatisticalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by plantdata-1007 on 2019/12/3.
 */
@Api(tags = "图谱配置")
@RestController
@RequestMapping("/config")
public class GraphConfStatisticalController {
    @Autowired
    private GraphConfStatisticalService graphConfStatisticalService;

    @ApiOperation("图谱配置-统计-新建")
    @PostMapping("/statistical/{kgName}")
    public ApiReturn<GraphConfStatisticalRsp> saveStatistical(@PathVariable("kgName") String kgName , @RequestBody @Valid GraphConfStatisticalReq req) {
        return ApiReturn.success(graphConfStatisticalService.createStatistical(kgName,req));
    }

    @ApiOperation("图谱配置-统计-批量新建")
    @PostMapping("/statistical/batch/save")
    public ApiReturn<List<GraphConfStatisticalRsp>> saveStatisticalBatch( @RequestBody @Valid List<GraphConfStatisticalReq> listReq) {
        return ApiReturn.success(graphConfStatisticalService.saveAll(listReq));
    }

    @ApiOperation("图谱配置-统计-更新")
    @PatchMapping("/statistical/{id}")
    public ApiReturn<GraphConfStatisticalRsp> updateStatistical(@PathVariable("id") Long id, @RequestBody @Valid GraphConfStatisticalReq req) {
        return ApiReturn.success(graphConfStatisticalService.updateStatistical(id, req));
    }

    @ApiOperation("图谱配置-统计-批量更新")
    @PatchMapping("/statistical/batch/update")
    public ApiReturn<List<GraphConfStatisticalRsp>> updateStatisticalBatch(@RequestBody List<Long> ids, @RequestBody @Valid List<GraphConfStatisticalReq> reqs) {
        return ApiReturn.success(graphConfStatisticalService.updateAll(ids , reqs));
    }

    @ApiOperation("图谱配置-统计-删除")
    @DeleteMapping("/statistical/{id}")
    public ApiReturn deleteStatistical(@PathVariable("id") Long id) {
        graphConfStatisticalService.deleteStatistical(id);
        return ApiReturn.success();
         }

    @ApiOperation("图谱配置-统计-批量删除")
    @PatchMapping("/statistical/batch/delete")
    public ApiReturn deleteStatisticalBatch(@RequestBody List<Long> ids) {
        graphConfStatisticalService.deleteInBatch(ids);
        return ApiReturn.success();
    }

    @ApiOperation("图谱配置-统计-查询")
    @GetMapping("/statistical/{kgName}")
    public ApiReturn<List<GraphConfStatisticalRsp>> selectStatistical(@PathVariable("kgName") String kgName) {
        return ApiReturn.success(graphConfStatisticalService.findByKgName(kgName));
    }

    @ApiOperation("图谱配置-统计-分页")
    @GetMapping("/statistical/page/{kgName}")
    public ApiReturn<Page<GraphConfStatisticalRsp>> selectStatisticalPage(@PathVariable("kgName") String kgName , BaseReq baseReq) {
        return ApiReturn.success(graphConfStatisticalService.getByKgName(kgName,baseReq));
    }
}
