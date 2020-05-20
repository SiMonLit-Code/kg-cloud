package com.plantdata.kgcloud.domain.app.controller;

import ai.plantdata.kg.api.pub.SparqlApi;
import ai.plantdata.kg.api.pub.resp.QueryResultVO;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.constant.ExportTypeEnum;
import com.plantdata.kgcloud.domain.app.controller.module.GraphAppInterface;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.app.service.KgDataService;
import com.plantdata.kgcloud.domain.common.util.EnumUtils;
import com.plantdata.kgcloud.domain.common.util.KGUtil;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.app.EntityQueryWithConditionReq;
import com.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import com.plantdata.kgcloud.sdk.req.app.SparQlReq;
import com.plantdata.kgcloud.sdk.rsp.app.sparql.QueryResultRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 10:59
 */
@RestController
@RequestMapping("kgdata")
public class KgDataController implements GraphAppInterface {


    @Autowired
    private KgDataService kgDataService;
    @Autowired
    private SparqlApi sparqlApi;
    @Autowired
    private HttpServletResponse response;

    @ApiOperation("sparql查询")
    @PostMapping("sparQl/query/{kgName}")
    public ApiReturn<QueryResultRsp> sparQlQuery(@PathVariable("kgName") String kgName, @RequestBody SparQlReq sparQlReq) {
        Optional<QueryResultVO> resOpt = RestRespConverter.convert(sparqlApi.query(KGUtil.dbName(kgName), sparQlReq.getQuery(), sparQlReq.getSize()));
        if (!resOpt.isPresent()) {
            return ApiReturn.success(new QueryResultRsp());
        }
        QueryResultRsp copy = BasicConverter.copy(resOpt.get(), QueryResultRsp.class);
        return ApiReturn.success(copy);
    }

    @ApiOperation("sparkSql结果导出")
    @GetMapping("export/{kgName}")
    public ApiReturn sparkSqlExport(@PathVariable("kgName") String kgName,
                                    @RequestParam(value = "query") String query,
                                    @RequestParam(value = "size", required = false) int size,
                                    @ApiParam("导出格式 0 txt  1 xls 2 xlsx") @RequestParam("type") int type) throws IOException {
        Optional<ExportTypeEnum> typeOpt = EnumUtils.parseById(ExportTypeEnum.class, type);
        if (!typeOpt.isPresent()) {
            throw BizException.of(AppErrorCodeEnum.EXPORT_TYPE_ERROR);
        }
        kgDataService.sparkSqlExport(kgName, typeOpt.get(), query, size, response);
        return ApiReturn.success(kgName);
    }

    @ApiOperation("依据实体name和meaningTag查询实体id")
    @PostMapping({"{kgName}/entity/query"})
    public ApiReturn<List<OpenEntityRsp>> queryEntityByNameAndMeaningTag(@PathVariable("kgName") String kgName,
                                                                         @RequestBody List<EntityQueryWithConditionReq> conditionReqList) {
        List<OpenEntityRsp> openEntityRspList = kgDataService.queryEntityByNameAndMeaningTag(kgName, conditionReqList);
        return ApiReturn.success(openEntityRspList);
    }


}
