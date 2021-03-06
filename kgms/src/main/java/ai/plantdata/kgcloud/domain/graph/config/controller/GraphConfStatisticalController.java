package ai.plantdata.kgcloud.domain.graph.config.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.bean.BasePage;
import ai.plantdata.cloud.bean.BaseReq;
import ai.plantdata.kgcloud.domain.graph.config.service.GraphConfStatisticalService;
import ai.plantdata.kgcloud.sdk.req.GraphConfStatisticalReq;
import ai.plantdata.kgcloud.sdk.req.UpdateGraphConfStatisticalReq;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfStatisticalRsp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author jiangdeming
 * @date 2019/12/3
 */
@Api(tags = "图谱配置")
@RestController
@RequestMapping("/config")
public class GraphConfStatisticalController {
    @Autowired
    private GraphConfStatisticalService graphConfStatisticalService;

    @ApiOperation("图谱配置-统计-新建")
    @PostMapping("/statistical/{kgName}")
    public ApiReturn<GraphConfStatisticalRsp> saveStatistical(@PathVariable("kgName") String kgName, @RequestBody @Valid GraphConfStatisticalReq req) {
        return ApiReturn.success(graphConfStatisticalService.createStatistical(kgName, req));
    }

    @ApiOperation("图谱配置-统计-批量新建")
    @PostMapping("/statistical/batch/save")
    public ApiReturn<List<GraphConfStatisticalRsp>> saveStatisticalBatch(@RequestBody @Valid List<GraphConfStatisticalReq> listReq) {
        return ApiReturn.success(graphConfStatisticalService.saveAll(listReq));
    }

    @ApiOperation("图谱配置-统计-更新")
    @PutMapping("/statistical/{id}")
    public ApiReturn<GraphConfStatisticalRsp> updateStatistical(@PathVariable("id") Long id, @RequestBody @Valid GraphConfStatisticalReq req) {
        return ApiReturn.success(graphConfStatisticalService.updateStatistical(id, req));
    }

    @ApiOperation("图谱配置-统计-批量更新")
    @PutMapping("/statistical/batch/update")
    public ApiReturn<List<GraphConfStatisticalRsp>> updateStatisticalBatch(@RequestBody @Valid List<UpdateGraphConfStatisticalReq> reqs) {
        return ApiReturn.success(graphConfStatisticalService.updateAll(reqs));
    }

    @ApiOperation("图谱配置-统计-删除")
    @DeleteMapping("/statistical/{id}")
    public ApiReturn deleteStatistical(@PathVariable("id") Long id) {
        graphConfStatisticalService.deleteStatistical(id);
        return ApiReturn.success();
    }

    @ApiOperation("图谱配置-统计-批量删除")
    @PutMapping("/statistical/batch/delete")
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
    public ApiReturn<BasePage<GraphConfStatisticalRsp>> selectStatisticalPage(@PathVariable("kgName") String kgName, BaseReq baseReq) {
        return ApiReturn.success(graphConfStatisticalService.getByKgName(kgName, baseReq));
    }
}
