package ai.plantdata.kgcloud.domain.graph.config.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.sdk.req.GraphConfFocusReq;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfFocusRsp;
import ai.plantdata.kgcloud.domain.graph.config.service.GraphConfFocusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-06 10:58
 **/
@Api(tags = "图谱配置")
@RestController
@RequestMapping("/config")
public class GraphConfigFocusController {

    @Autowired
    GraphConfFocusService graphConfFocusService;

    @ApiOperation("图谱配置-焦点-获取")
    @GetMapping("/focus/{kgName}")
    public ApiReturn<List<GraphConfFocusRsp>> find(@PathVariable("kgName") String kgName) {
        return ApiReturn.success(graphConfFocusService.findByKgName(kgName));
    }

    @ApiOperation("图谱配置-焦点-保存")
    @PutMapping("/focus/{kgName}")
    public ApiReturn<List<GraphConfFocusRsp>> saveFocus(@PathVariable("kgName") String kgName, @RequestBody @Valid List<GraphConfFocusReq> req) {
        return ApiReturn.success(graphConfFocusService.save(kgName, req));
    }
}
