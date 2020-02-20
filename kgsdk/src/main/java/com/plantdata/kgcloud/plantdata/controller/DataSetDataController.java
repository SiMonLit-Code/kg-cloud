package com.plantdata.kgcloud.plantdata.controller;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.plantdata.converter.common.ApiReturnConverter;
import com.plantdata.kgcloud.plantdata.converter.dataset.DataSetConverter;
import com.plantdata.kgcloud.plantdata.req.dataset.InterfaceUploadParameter;
import com.plantdata.kgcloud.plantdata.req.dataset.ReadParameter;
import com.plantdata.kgcloud.plantdata.req.dataset.SearchParameter;
import com.plantdata.kgcloud.plantdata.req.dataset.TwoDimensionalParameter;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.KgDataClient;
import com.plantdata.kgcloud.sdk.req.StatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.app.DataSetStatisticRsp;
import com.plantdata.kgcloud.sdk.req.app.dataset.DataSetAddReq;
import com.plantdata.kgcloud.sdk.req.app.dataset.NameReadReq;
import com.plantdata.kgcloud.sdk.rsp.app.RestData;
import com.plantdata.kgcloud.util.JacksonUtils;
import com.plantdata.kgcloud.util.JsonUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Administrator
 */
@RestController("dataSetDataController-v2")
@RequestMapping("sdk/data")
public class DataSetDataController implements SdkOldApiInterface {

    @Autowired
    private KgDataClient kgDataClient;
    @Autowired
    private AppClient appClient;

    @ApiOperation("读数普通据集的数据")
    @PostMapping("read")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataName", required = true, dataType = "string", paramType = "form", value = "dataName"),
            @ApiImplicitParam(name = "query", dataType = "string", paramType = "form", value = "查询条件,mongo语法"),
            @ApiImplicitParam(name = "fields", dataType = "string", paramType = "form", value = "返回字段,json数组格式,默认返回全部字段"),
            @ApiImplicitParam(name = "sort", dataType = "string", paramType = "form", value = "排序,mongo语法"),
            @ApiImplicitParam(name = "pageNo", dataType = "string", paramType = "query", value = "pageNo"),
            @ApiImplicitParam(name = "pageSize", dataType = "string", paramType = "query", value = "pageSize"),
    })
    public RestResp<RestData<Map<String, Object>>> read(@Valid @ApiIgnore ReadParameter readParameter) {
        Function<ReadParameter, NameReadReq> reqFunction = DataSetConverter::readParameterToNameReadReq;
        ApiReturn<RestData<Map<String, Object>>> restDataApiReturn = kgDataClient.searchDataSet(reqFunction.apply(readParameter));
        RestData<Map<String, Object>> data = ApiReturnConverter.convert(restDataApiReturn);
        return new RestResp<>(data);
    }

    @ApiOperation("读数搜索据集的数据")
    @PostMapping("search")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataName", required = true, dataType = "string", paramType = "form", value = "dataName"),
            @ApiImplicitParam(name = "query", dataType = "string", paramType = "form", value = "查询条件,es语法"),
            @ApiImplicitParam(name = "fields", dataType = "string", paramType = "form", value = "返回字段,json数组格式,默认返回全部字段"),
            @ApiImplicitParam(name = "sort", dataType = "string", paramType = "form", value = "排序,es语法"),
            @ApiImplicitParam(name = "pageNo", dataType = "string", paramType = "query", value = "pageNo"),
            @ApiImplicitParam(name = "pageSize", dataType = "string", paramType = "query", value = "pageSize"),

    })
    public RestResp<RestData<Map<String, Object>>> search(@Valid @ApiIgnore SearchParameter searchParameter) {
        Function<SearchParameter, NameReadReq> reqFunction = DataSetConverter::searchParameterToNameReadReq;
        ApiReturn<RestData<Map<String, Object>>> restDataApiReturn = kgDataClient.searchDataSet(reqFunction.apply(searchParameter));
        RestData<Map<String, Object>> data = ApiReturnConverter.convert(restDataApiReturn);
        return new RestResp<>(data);
    }


    @PostMapping("/add")
    @ApiOperation("数据集导入数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "data", required = true, dataType = "string", paramType = "form", value = "data"),
            @ApiImplicitParam(name = "dataName", dataType = "string", paramType = "form", value = "数据集标识"),

    })
    public RestResp interfaceUpload(@Valid @ApiIgnore InterfaceUploadParameter param) {
        List<Map<String, Object>> data = null;
        try {
            data = JacksonUtils.readValue(param.getData(), new TypeReference<List<Map<String, Object>>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        kgDataClient.batchSaveDataSetByName(new DataSetAddReq(data, param.getDataName()));
        return new RestResp<>(true);
    }

    @ApiOperation("统计数据二维")
    @PostMapping("stat/2d")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataName", required = true, dataType = "string", paramType = "form", value = "数据集的唯一标识"),
            @ApiImplicitParam(name = "query", dataType = "string", paramType = "form", value = "检索条件，es语法"),
            @ApiImplicitParam(name = "aggs", required = true, dataType = "string", paramType = "form", value = "统计条件，es语法，样例：{\"by_key1\":{\"terms\":{\"field\":\"keywords\"}}}"),
            @ApiImplicitParam(name = "returnType", defaultValue = "0", dataType = "string", paramType = "form", value = "返回格式，0Echart,1普通格式"),
            @ApiImplicitParam(name = "pageSize", dataType = "string", paramType = "query", value = "pageSize"),
    })
    public RestResp<DataSetStatisticRsp> statTwoDimensional(@Valid @ApiIgnore TwoDimensionalParameter twoDimensional) {
        StatisticByDimensionalReq dimensionalReq = DataSetConverter.twoDimensionalParameterToStatisticByDimensionalReq(twoDimensional);
        DataSetStatisticRsp data = ApiReturnConverter.convert(appClient.statistic2d(twoDimensional.getDataName(), dimensionalReq));
        return new RestResp<>(data);
    }

    @ApiOperation("统计数据三维")
    @PostMapping("stat/3d")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataName", required = true, dataType = "string", paramType = "form", value = "数据集的唯一标识"),
            @ApiImplicitParam(name = "query", dataType = "string", paramType = "form", value = "检索条件，es语法"),
            @ApiImplicitParam(name = "aggs", required = true, dataType = "string", paramType = "form", value = "统计条件，es语法，样例：{ \"by_key1\": {\"terms\": { \"field\": \"keywords\" } } }"),
            @ApiImplicitParam(name = "returnType", defaultValue = "0", dataType = "string", paramType = "form", value = "返回格式，0Echart,1普通格式"),
            @ApiImplicitParam(name = "pageSize", dataType = "string", paramType = "query", value = "pageSize"),
    })
    public RestResp<DataSetStatisticRsp> statThreeDimensional(@Valid @ApiIgnore TwoDimensionalParameter twoDimensional) {
        StatisticByDimensionalReq dimensionalReq = DataSetConverter.twoDimensionalParameterToStatisticByDimensionalReq(twoDimensional);
        DataSetStatisticRsp data = ApiReturnConverter.convert(appClient.statistic3d(twoDimensional.getDataName(), dimensionalReq));
        return new RestResp<>(data);
    }
}
