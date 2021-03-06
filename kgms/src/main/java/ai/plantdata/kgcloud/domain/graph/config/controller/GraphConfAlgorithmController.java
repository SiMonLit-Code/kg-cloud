package ai.plantdata.kgcloud.domain.graph.config.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.domain.graph.config.service.GraphConfAlgorithmService;
import ai.plantdata.kgcloud.sdk.req.GraphConfAlgorithmReq;
import ai.plantdata.kgcloud.sdk.req.GraphConfAlgorithmReqList;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfAlgorithmRsp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 图谱算法配置
 *
 * @author jiangdeming
 * @date 2019/11/29
 */
@Api(tags = "图谱配置")
@RestController
@RequestMapping("/config")
public class GraphConfAlgorithmController {
    @Autowired
    private GraphConfAlgorithmService graphConfAlgorithmService;

    @ApiOperation("图谱配置-算法-新建")
    @PostMapping("/algorithm/{kgName}")
    public ApiReturn<GraphConfAlgorithmRsp> save(@PathVariable("kgName") String kgName, @RequestBody @Valid GraphConfAlgorithmReq req) {
        return ApiReturn.success(graphConfAlgorithmService.createAlgorithm(kgName, req));
    }

    @ApiOperation("图谱配置-算法-更新")
    @PutMapping("/algorithm/{id}")
    public ApiReturn<GraphConfAlgorithmRsp> update(@PathVariable("id") Long id, @RequestBody @Valid GraphConfAlgorithmReq req) {
        return ApiReturn.success(graphConfAlgorithmService.updateAlgorithm(id, req));
    }

    @ApiOperation("图谱配置-算法-删除")
    @DeleteMapping("/algorithm/{id}")
    public ApiReturn delete(@PathVariable("id") Long id) {
        graphConfAlgorithmService.deleteAlgorithm(id);
        return ApiReturn.success();
    }

    @ApiOperation("图谱配置-算法-获取")
    @GetMapping("/algorithm/{kgName}")
    public ApiReturn<Page<GraphConfAlgorithmRsp>> select(@PathVariable("kgName") String kgName, GraphConfAlgorithmReqList baseReq) {
        return ApiReturn.success(graphConfAlgorithmService.findByKgName(kgName, baseReq));
    }

    @ApiOperation("图谱配置-算法-详情")
    @GetMapping("/algorithm/detail/{id}")
    public ApiReturn<GraphConfAlgorithmRsp> detailAlgorithm(@PathVariable("id") Long id) {
        return ApiReturn.success(graphConfAlgorithmService.findById(id));
    }
}
