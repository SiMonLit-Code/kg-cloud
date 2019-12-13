package com.plantdata.kgcloud.domain.app.controller;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.app.service.KgDataService;
import com.plantdata.kgcloud.domain.dataset.service.DataOptService;
import com.plantdata.kgcloud.domain.model.service.ModelService;
import com.plantdata.kgcloud.sdk.req.app.SparQlReq;
import com.plantdata.kgcloud.sdk.req.app.dataset.DataSetAddReq;
import com.plantdata.kgcloud.sdk.req.app.dataset.NameReadReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeAttrStatisticByAttrValueReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByConceptIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByEntityIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByAttrIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByConceptReq;
import com.plantdata.kgcloud.sdk.rsp.app.RestData;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.Api;
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

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 10:59
 */
@RestController
@RequestMapping("kgdata")
@Api(tags = "sdk使用")
public class KgDataController {


    @Autowired
    private ModelService modelService;
    @Autowired
    private KgDataService kgDataService;
    @Autowired
    private DataOptService dataOptService;

    @ApiOperation("sparql查询")
    @PostMapping("sparQl/query/{kgName}")
    public ApiReturn<Object> sparQlQuery(@PathVariable("kgName") String kgName, @RequestBody SparQlReq sparQlReq) {
        //todo 底层提供api
        return ApiReturn.success();
    }

    @ApiOperation("第三方模型抽取")
    @PostMapping("extract/thirdModel/{modelId}")
    public ApiReturn<Object> extractThirdModel(@PathVariable("modelId") Long modelId,
                                               @RequestParam("input") String input, @RequestBody List<Map<String, String>> configList) {
        return ApiReturn.success(modelService.call(modelId, Lists.newArrayList(input)));
    }

    @ApiOperation("sparkSql结果导出")
    @GetMapping("export/{kgName}")
    public ApiReturn sparkSqlExport(@PathVariable("kgName") String kgName, @ApiParam(value = "查询语句", required = true) @RequestParam("query") String query,
                                    @ApiParam("返回数量") int size,
                                    @ApiParam("导出格式 0 txt  1 xls 2 xlsx") @RequestParam("type") int type) {
        return ApiReturn.success(null);
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

    @ApiOperation("查询实体的关系度数")
    @PostMapping("/statistic/{kgName}/entity/degree/")
    public ApiReturn<List<Map<String, Object>>> statisticCountEdgeByEntity(@PathVariable("kgName") String kgName,
                                                                           @RequestBody EdgeStatisticByEntityIdReq statisticReq) {
        return ApiReturn.success(kgDataService.statisticCountEdgeByEntity(kgName, statisticReq));

    }

    @ApiOperation("统计实体根据概念分组")
    @PostMapping("statistic/{kgName}/entity/groupByConcept/")
    public ApiReturn<Object> statisticEntityGroupByConcept(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                           @RequestBody EntityStatisticGroupByConceptReq statisticReq) {
        return ApiReturn.success(kgDataService.statEntityGroupByConcept(kgName, statisticReq));
    }

    @ApiOperation("实体属性值统计")
    @PostMapping("statistic/{kgName}/attr/groupByAttrValue")
    public ApiReturn<Object> statisticAttrGroupByConcept(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                         @RequestBody EntityStatisticGroupByAttrIdReq statisticReq) {
        return ApiReturn.success(kgDataService.statisticAttrGroupByConcept(kgName, statisticReq));
    }

    @ApiOperation("对象属性统计，统计对象属性的数量，按关系分组")
    @PostMapping("statistic/{kgName}/edge/groupByAttrName")
    public ApiReturn<Object> statisticRelation(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                               @RequestBody EdgeStatisticByConceptIdReq statisticReq) {
        return ApiReturn.success(kgDataService.statisticRelation(kgName, statisticReq));
    }

    @ApiOperation("边数值属性统计，按数值属性值分组")
    @PostMapping("statistic/{kgName}/edgeAttr/groupByAttrValue")
    public ApiReturn<Object> statEdgeGroupByEdgeValue(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                      EdgeAttrStatisticByAttrValueReq statisticReq) {
        return ApiReturn.success(kgDataService.statEdgeGroupByEdgeValue(kgName, statisticReq));
    }

    @ApiOperation("读取数据集")
    @PostMapping("dataset/read")
    public ApiReturn<RestData<Map<String, Object>>> searchDataSet(NameReadReq nameReadReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(kgDataService.searchDataSet(userId, nameReadReq));
    }

    @ApiOperation("新增数据集")
    @PostMapping("dataset/name")
    public ApiReturn batchSaveDataSetByName(@RequestBody DataSetAddReq addReq) {
        String userId = SessionHolder.getUserId();
        dataOptService.batchAddDataForDataSet(userId, addReq);
        return ApiReturn.success();
    }
}
