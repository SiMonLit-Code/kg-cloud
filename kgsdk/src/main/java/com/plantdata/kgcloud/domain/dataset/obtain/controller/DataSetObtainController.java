package com.plantdata.kgcloud.domain.dataset.obtain.controller;

import com.plantdata.kgcloud.domain.common.module.DataSetDataObtainInterface;
import com.plantdata.kgcloud.domain.dataset.obtain.service.DataSetObtainService;
import com.plantdata.kgcloud.domain.dataset.statistic.controller.req.BaseTableReq;
import com.plantdata.kgcloud.domain.dataset.statistic.controller.req.InterfaceUploadReq;
import com.plantdata.kgcloud.domain.dataset.statistic.controller.req.ReadTableReq;
import com.plantdata.kgcloud.domain.dataset.statistic.controller.req.SearchTableReq;
import com.plantdata.kgcloud.bean.ApiReturn;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/8 10:56
 */
@RestController
@RequestMapping("dataSet/obtain")
public class DataSetObtainController implements DataSetDataObtainInterface {

//    @Resource
//    private DataSetObtainService dataSetObtainService;
//
//    @ApiOperation("读数搜索据集的数据")
//    @PostMapping("searchByTable")
//    public ApiReturn<DataCountRsp<Map<String, Object>>> search(@RequestBody @Valid SearchTableReq searchTableParam) {
//        return ApiReturn.success(dataSetObtainService.readSearch(searchTableParam));
//    }
//
//    @ApiOperation("读数普通据集的数据")
//    @PostMapping("readByTable")
//    public ApiReturn<DataCountRsp<Map<String, Object>>> read(@RequestBody @Valid ReadTableReq readTableParam) {
//        return ApiReturn.success(dataSetObtainService.readCommonByTable(readTableParam));
//    }
//
//    @ApiOperation("读数普通据集的数据")
//    @PostMapping("read/{dataSetKey}")
//    public ApiReturn<DataCountRsp<Map<String, Object>>> read(
//            @PathVariable("dataSetKey") String dataSetKey,
//            @RequestBody @Valid BaseTableReq readParam) {
//        return ApiReturn.success(dataSetObtainService.readCommonByDataSetKey(dataSetKey, readParam));
//    }
//
//    @ApiOperation("读数搜索据集的数据")
//    @PostMapping("search/{dataSetKey}")
//    public ApiReturn<DataCountRsp<Map<String, Object>>> search(@PathVariable("dataSetKey") String dataSetKey,
//                                                               @RequestBody @Valid BaseTableReq readParam) {
//        return ApiReturn.success(dataSetObtainService.readSearchByDataSetKey(dataSetKey, readParam));
//    }
//
//    @PostMapping("add")
//    @ApiOperation("数据集导入数据")
//    public ApiReturn interfaceUpload(@Valid InterfaceUploadReq interfaceUpload) {
//        return ApiReturn.success(true);
//    }


}
