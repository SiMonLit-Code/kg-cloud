package com.plantdata.kgcloud.domain.graph.clash.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.domain.graph.clash.entity.ClashListReq;
import com.plantdata.kgcloud.domain.graph.clash.entity.ClashToGraphReq;
import com.plantdata.kgcloud.domain.graph.clash.service.ClashService;
import com.plantdata.kgcloud.util.JacksonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author xiezhenxiang 2019/12/12
 */
@RequestMapping("/clash")
@RestController
@Api(tags = "冲突数据")
public class ClashDataController {

    @Autowired
    private ClashService clashService;

    @GetMapping("/clash/{kgName}")
    @ApiOperation("列表")
    public ApiReturn<Map> list(@PathVariable("kgName") String kgName,
                               @RequestBody ClashListReq req) {
        Map<String, Object> page = clashService.list(kgName, req);
        return ApiReturn.success(page);
    }

    @PostMapping("clash/to/graph/{kgName}")
    @ApiOperation("入图")
    public ApiReturn toGraph(@PathVariable("kgName") String kgName,
                             @Valid @RequestBody ClashToGraphReq request) {
        clashService.toGraph(kgName, request);
        return ApiReturn.success();
    }

    @PostMapping("/clash/{kgName}")
    @ApiOperation("删除")
    public ApiReturn remove(@PathVariable("kgName") String kgName,
                            @ApiParam("id数组") @RequestParam("ids") String ids) {
        List<String> ls = JacksonUtils.readValue(ids, new TypeReference<List<String>>(){});
        clashService.delete(kgName, ls);
        return ApiReturn.success();
    }
}



