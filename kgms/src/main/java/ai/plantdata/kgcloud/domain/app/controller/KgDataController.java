package ai.plantdata.kgcloud.domain.app.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.bean.BasePage;
import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.kg.api.pub.SparqlApi;
import ai.plantdata.kg.api.pub.resp.QueryResultVO;
import ai.plantdata.kgcloud.constant.AppErrorCodeEnum;
import ai.plantdata.kgcloud.constant.ExportTypeEnum;
import ai.plantdata.kgcloud.domain.app.controller.module.GraphAppInterface;
import ai.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import ai.plantdata.kgcloud.domain.app.converter.BasicConverter;
import ai.plantdata.kgcloud.domain.app.service.KgDataService;
import ai.plantdata.kgcloud.domain.common.util.EnumUtils;
import ai.plantdata.kgcloud.domain.common.util.KGUtil;
import ai.plantdata.kgcloud.sdk.req.app.EntityQueryWithConditionReq;
import ai.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import ai.plantdata.kgcloud.sdk.req.app.SparQlReq;
import ai.plantdata.kgcloud.sdk.req.app.TraceabilityQueryReq;
import ai.plantdata.kgcloud.sdk.rsp.app.sparql.QueryResultRsp;
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

    @ApiOperation("根据溯源信息查询实体")
    @PostMapping({"{kgName}/source/entity/query"})
    public ApiReturn<BasePage<OpenEntityRsp>> queryEntityBySource(@PathVariable("kgName") String kgName,
                                                                  @RequestBody TraceabilityQueryReq req) {
        BasePage<OpenEntityRsp> entitys = kgDataService.queryEntityBySource(kgName, req);
        return ApiReturn.success(entitys);
    }


}
