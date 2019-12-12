package com.plantdata.kgcloud.domain.scene.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.scene.service.GraphService;
import com.plantdata.kgcloud.sdk.req.GraphPageReq;
import com.plantdata.kgcloud.sdk.rsp.GraphRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

@Api(tags = "图谱")
@RestController
@RequestMapping("/graph")
public class GraphController {


    @Autowired
    private GraphService graphService;

    @ApiOperation("图谱列表")
    @PostMapping("/get/list")
    public ApiReturn<List<GraphRsp>> getListPage() {
        return graphService.getListPage();
    }

    @ApiOperation("获取schema")
    @PostMapping("/schema")
    public ApiReturn<SchemaRsp> getSchema(String kgName) {
        return graphService.getSchema(kgName);
    }

}
