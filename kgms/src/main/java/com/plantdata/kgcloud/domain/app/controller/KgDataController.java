package com.plantdata.kgcloud.domain.app.controller;

import ai.plantdata.kg.api.pub.SparqlApi;
import ai.plantdata.kg.api.pub.resp.QueryResultVO;
import ai.plantdata.kg.common.bean.BasicInfo;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.constant.ExportTypeEnum;
import com.plantdata.kgcloud.domain.app.controller.module.GraphAppInterface;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.app.service.KgDataService;
import com.plantdata.kgcloud.domain.common.util.EnumUtils;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.model.service.ModelService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.app.SparQlReq;
import com.plantdata.kgcloud.sdk.rsp.app.sparql.QueryResultRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 10:59
 */
@RestController
@RequestMapping("kgdata")
public class KgDataController implements GraphAppInterface {


    @Autowired
    private ModelService modelService;
    @Autowired
    private KgDataService kgDataService;
    @Autowired
    private SparqlApi sparqlApi;
    @Autowired
    private HttpServletResponse response;

    @ApiOperation("sparql查询")
    @PostMapping("sparQl/query/{kgName}")
    public ApiReturn<QueryResultRsp> sparQlQuery(@PathVariable("kgName") String kgName, @RequestBody SparQlReq sparQlReq) {
        Optional<QueryResultVO> resOpt = RestRespConverter.convert(sparqlApi.query(kgName, sparQlReq.getQuery(), sparQlReq.getSize()));
        if (!resOpt.isPresent()) {
            return ApiReturn.success(new QueryResultRsp());
        }
        QueryResultRsp copy = BasicConverter.copy(resOpt.get(), QueryResultRsp.class);
        return ApiReturn.success(copy);
    }

    @ApiOperation("第三方模型抽取")
    @PostMapping("extract/thirdModel/{modelId}")
    public ApiReturn<Object> extractThirdModel(@PathVariable("modelId") Long modelId,
                                               @RequestParam("input") String input,
                                               @RequestParam("config") String config) {
        return ApiReturn.success(modelService.call(modelId, Lists.newArrayList(input)));
    }

    @ApiOperation("sparkSql结果导出")
    @GetMapping("export/{kgName}")
    public ApiReturn sparkSqlExport(@PathVariable("kgName") String kgName, @RequestBody SparQlReq sparQlReq,
                                    @ApiParam("导出格式 0 txt  1 xls 2 xlsx") @RequestParam("type") int type) throws IOException {
        Optional<ExportTypeEnum> typeOpt = EnumUtils.parseById(ExportTypeEnum.class, type);
        if (!typeOpt.isPresent()) {
            throw BizException.of(AppErrorCodeEnum.EXPORT_TYPE_ERROR);
        }
        kgDataService.sparkSqlExport(kgName, typeOpt.get(), sparQlReq, response);
        return ApiReturn.success(kgName);
    }

    @PostMapping("concept/entity")
    @ApiOperation("实体概念标注")
    public ApiReturn entity(@ApiParam(required = true) @RequestParam("kgName") String kgName,
                            @ApiParam(required = true) @RequestParam("dataSetId") Integer dataSetId,
                            @ApiParam("需要标注的字段和权重，如不指定表示使用所有字段且权重相同") @RequestParam("fieldsAndWeights") String fieldsAndWeights,
                            @ApiParam(required = true) @RequestParam("targetConcepts") String targetConcepts,
                            @ApiParam(required = true) @RequestParam("traceConfig") String traceConfig,
                            @ApiParam("默认使用算法1，目前三种算法分别为：1：基础标注算法:2：语义相似标注算法:3：图传播标注算法") @RequestParam("algorithms") String algorithms) {
        //todo 改不了
        return null;//taggingService.tagging(kgName, dataSetId, fieldsAndWeights, targetConcepts, traceConfig, algorithms);
    }

    @ApiOperation("依据实体name和meaningTag查询实体id")
    @PostMapping({"{kgName}/entity/name/meaningTag"})
    public ApiReturn<Map<String, Set<Long>>> getIdByNameAndMeaningTag(@RequestParam("kgName") String var1, @RequestBody List<BasicInfo> var2) {
        //todo
        return ApiReturn.success();
    }


}
