package com.plantdata.kgcloud.domain.app.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.app.controller.module.SdkOpenApiInterface;
import com.plantdata.kgcloud.domain.app.service.KgDataService;
import com.plantdata.kgcloud.domain.dataset.service.DataOptService;
import com.plantdata.kgcloud.sdk.req.app.dataset.DataSetAddReq;
import com.plantdata.kgcloud.sdk.req.app.dataset.DataSetOneFieldReq;
import com.plantdata.kgcloud.sdk.req.app.dataset.NameReadReq;
import com.plantdata.kgcloud.sdk.rsp.app.RestData;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/19 13:37
 */
@RestController("openDataSetController")
@RequestMapping("kgdata/dataset")
public class DataSetController implements SdkOpenApiInterface {
    @Autowired
    private KgDataService kgDataService;
    @Autowired
    private DataOptService dataOptService;

    @ApiOperation("读取数据集(单字段)")
    @PostMapping("read/{dataName}")
    public ApiReturn<List<Object>> searchDataSet(
            @PathVariable("dataName") String dataName,
            @RequestBody DataSetOneFieldReq fieldReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(kgDataService.readDataSetData(userId, dataName, fieldReq));
    }

    @ApiOperation("读取数据集")
    @PostMapping("read")
    public ApiReturn<RestData<Map<String, Object>>> searchDataSet(@RequestBody NameReadReq nameReadReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(kgDataService.searchDataSet(userId, nameReadReq));
    }

    @ApiOperation("向数据集中新增数据")
    @PostMapping("name")
    public ApiReturn batchSaveDataSetByName(@RequestBody DataSetAddReq addReq) {
        String userId = SessionHolder.getUserId();
        dataOptService.batchAddDataForDataSet(userId, addReq);
        return ApiReturn.success();
    }
}
