package com.plantdata.kgcloud.domain.data.obtain.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphDataObtainInterface;
import com.plantdata.kgcloud.sdk.EditClient;
import com.plantdata.kgcloud.sdk.KgDataClient;
import com.plantdata.kgcloud.sdk.SemanticClient;
import com.plantdata.kgcloud.sdk.req.app.SparQlReq;
import com.plantdata.kgcloud.sdk.req.app.sematic.GremlinReq;
import com.plantdata.kgcloud.sdk.req.edit.KgqlReq;
import com.plantdata.kgcloud.sdk.rsp.app.GremlinRsp;
import com.plantdata.kgcloud.sdk.rsp.app.sparql.QueryResultRsp;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author cjw
 * @date 2020/7/7  14:56
 */
@RestController
@RequestMapping("v3/kgdata/query")
public class QueryController implements GraphDataObtainInterface {

    @Autowired
    private EditClient editClient;
    @Autowired
    private SemanticClient semanticClient;
    @Autowired
    private KgDataClient kgDataClient;

    @ApiOperation(value = "kgql查询", notes = "KGQL查询，使用PlantData知识图谱的查询语言查询数据。")
    @PostMapping("kgql")
    public ApiReturn executeQl(@Valid @RequestBody KgqlReq kgqlReq) {
        return editClient.executeQl(kgqlReq);
    }

    @ApiOperation(value = "gremlin查询", notes = "使用gremlin图查询语言查询数据。")
    @PostMapping("gremlin/{kgName}")
    public ApiReturn<GremlinRsp> gremlinQuery(@PathVariable("kgName") String kgName, @RequestBody GremlinReq req) {
        return semanticClient.gremlinQuery(kgName, req);
    }

    @ApiOperation(value = "sparQl查询", notes = "使用sparQl查询语言查询数据。")
    @PostMapping("sparQl/{kgName}")
    public ApiReturn<QueryResultRsp> sparQlQuery(@PathVariable("kgName") String kgName, @RequestBody SparQlReq sparQlReq) {
        return kgDataClient.sparQlQuery(kgName, sparQlReq);
    }
}
