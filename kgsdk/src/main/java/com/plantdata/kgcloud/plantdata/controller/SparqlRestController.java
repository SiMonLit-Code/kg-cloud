package com.plantdata.kgcloud.plantdata.controller;


import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.converter.spark.SparkConverter;
import com.plantdata.kgcloud.plantdata.req.app.SparqlQueryParameter;
import com.plantdata.kgcloud.sdk.KgDataClient;
import com.plantdata.kgcloud.sdk.req.app.SparQlReq;
import com.plantdata.kgcloud.sdk.rsp.app.sparql.QueryResultRsp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Optional;


/**
 * @author Administrator
 */
@RestController
@RequestMapping("sdk/sparql")
@Api(tags = "sparql-sdk")
public class SparqlRestController {

    @Autowired
    private KgDataClient kgDataClient;

    @ApiOperation("sparql查询")
    @PostMapping("query")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "query", required = true, dataType = "string", paramType = "form", value = "查询问题"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query", value = "返回数量")
    })
    public RestResp<QueryResultRsp> sparqlQuery(@Valid @ApiIgnore SparqlQueryParameter param) {
        SparQlReq sparQlReq = SparkConverter.sparqlQueryParameterToSparQlReq(param);
        Optional<QueryResultRsp> queryResultRsp = BasicConverter.apiReturnData(kgDataClient.sparQlQuery(param.getKgName(), sparQlReq));
        return new RestResp<>(queryResultRsp.orElse(new QueryResultRsp()));
    }
}
