package com.plantdata.kgcloud.domain.graph.source.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.graph.source.req.GraphQueryReq;
import com.plantdata.kgcloud.domain.graph.source.req.GraphSourceReq;
import com.plantdata.kgcloud.domain.graph.source.rsp.GraphSourceRsp;
import com.plantdata.kgcloud.domain.graph.source.service.GraphSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2020/3/21 16:03
 * @Description:
 */
@Api(tags = "图谱来源")
@RestController
@RequestMapping("source")
public class GraphSourceController {

    @Autowired
    private GraphSourceService graphSourceService;

    @ApiOperation("添加来源")
    @GetMapping("/{kgName}")
    public ApiReturn add(@PathVariable("kgName") String kgName, @RequestBody GraphSourceReq graphSourceReq) {
        return ApiReturn.success();
    }


    @ApiOperation("来源查询")
    @GetMapping("/{kgName}/query")
    public ApiReturn<List<GraphSourceRsp>> query(@PathVariable("kgName") String kgName, @RequestBody GraphQueryReq graphQueryReq) {
        return ApiReturn.success();
    }
}
