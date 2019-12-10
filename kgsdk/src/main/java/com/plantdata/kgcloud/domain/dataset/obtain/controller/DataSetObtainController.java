package com.plantdata.kgcloud.domain.dataset.obtain.controller;

import com.plantdata.kgcloud.domain.common.module.DataSetDataObtainInterface;
import com.plantdata.kgcloud.domain.dataset.statistic.controller.req.BaseTableReq;
import com.plantdata.kgcloud.domain.dataset.statistic.controller.req.ReadTableReq;
import com.plantdata.kgcloud.domain.dataset.statistic.controller.req.SearchTableReq;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/8 10:56
 */
@RestController
@RequestMapping("dataSet/obtain")
public class DataSetObtainController implements DataSetDataObtainInterface {
//
//    @Resource
//    private DataSetObtainService dataSetObtainService;
//
//    @ApiOperation("读数搜索据集的数据")
//    @PostMapping("searchByTable")
//    public ApiReturn<DataCountRsp<Map<String, Object>>> search(@RequestBody @Valid SearchTableReq searchTableParam) {
//        return ApiReturn.success(dataSetObtainService.readSearch(searchTableParam));
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
