package ai.plantdata.kgcloud.domain.graph.config.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.bean.BasePage;
import ai.plantdata.cloud.bean.BaseReq;
import ai.plantdata.kgcloud.domain.graph.config.service.GraphConfReasonService;
import ai.plantdata.kgcloud.sdk.req.GraphConfReasonReq;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfReasonRsp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author jdm
 * @date 2019/12/9 16:10
 */

@Api(tags = "图谱配置")
@RestController
@RequestMapping("/config")
public class GraphConfReasonController {

    @Autowired
    private GraphConfReasonService graphConfReasoningService;

    @ApiOperation("图谱配置-推理-新增")
    @PostMapping("/reason/{kgName}")
    public ApiReturn<GraphConfReasonRsp> saveReasoning(@PathVariable("kgName") String kgName, @RequestBody @Valid GraphConfReasonReq req) {
        return ApiReturn.success(graphConfReasoningService.createReasoning(kgName, req));
    }


    @ApiOperation("图谱配置-推理-分页")
    @GetMapping("/reason/page/{kgName}")
    public ApiReturn<BasePage<GraphConfReasonRsp>> selectReasoningPage(@PathVariable("kgName") String kgName, BaseReq baseReq) {
        return ApiReturn.success(graphConfReasoningService.getByKgName(kgName, baseReq));
    }

    @ApiOperation("图谱配置-推理-详情")
    @GetMapping("/reason/{id}")
    public ApiReturn<GraphConfReasonRsp> detailReasoning(@PathVariable("id") Long id) {
        return ApiReturn.success(graphConfReasoningService.findById(id));
    }

    @ApiOperation("图谱配置-推理-删除")
    @DeleteMapping("/reason/{id}")
    public ApiReturn deleteReasoning(@PathVariable("id") Long id) {
        graphConfReasoningService.deleteReasoning(id);
        return ApiReturn.success();
    }

    @ApiOperation("图谱配置-推理-更新")
    @PutMapping("/reason/{id}")
    public ApiReturn<GraphConfReasonRsp> updateReasoning(@PathVariable("id") Long id, @RequestBody @Valid GraphConfReasonReq req) {
        return ApiReturn.success(graphConfReasoningService.updateReasoning(id, req));
    }


}
