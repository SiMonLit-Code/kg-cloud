package com.plantdata.kgcloud.domain.dataset.obtain;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.domain.common.module.DataSetDataObtainInterface;
import com.plantdata.kgcloud.sdk.KgmsClient;
import com.plantdata.kgcloud.sdk.req.app.dataset.DataSetAddReq;
import com.plantdata.kgcloud.sdk.KgDataClient;
import com.plantdata.kgcloud.sdk.req.app.dataset.DataSetOneFieldReq;
import com.plantdata.kgcloud.sdk.req.app.dataset.NameReadReq;
import com.plantdata.kgcloud.sdk.rsp.DataSetRsp;
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
 * @date 2019/11/8 10:56
 */
@RestController
@RequestMapping("v3/dataset/obtain")
public class DataSetObtainController implements DataSetDataObtainInterface {

    @Autowired
    private KgDataClient kgDataClient;
    @Autowired
    private KgmsClient kgmsClient;

    @ApiOperation("获取数据集列表")
    @GetMapping("page")
    public ApiReturn<BasePage<DataSetRsp>> dataSetList(@RequestParam(value = "kw", required = false) String kw,
                                                       @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        return kgmsClient.dataSetFindAll(kw, page, size);
    }


    @ApiOperation("读数普通数据集的数据")
    @PostMapping("common")
    public ApiReturn<RestData<Map<String, Object>>> searchDataSet(@RequestBody NameReadReq nameReadReq) {
        return kgDataClient.searchDataSet(nameReadReq);
    }

    @ApiOperation("读取数据集(单字段)")
    @PostMapping("read/{dataName}")
    public ApiReturn<List<Object>> searchDataSet(
            @PathVariable("dataName") String dataName,
            @RequestBody DataSetOneFieldReq fieldReq) {
        return kgDataClient.searchDataSet(dataName, fieldReq);
    }


    @ApiOperation("读数搜索数据集的数据")
    @PostMapping("search")
    public ApiReturn<RestData<Map<String, Object>>> search(@RequestBody NameReadReq nameReadReq) {
        return kgDataClient.searchDataSet(nameReadReq);
    }

    @PostMapping("add")
    @ApiOperation("数据集导入数据")
    public ApiReturn interfaceUpload(@RequestBody DataSetAddReq addReq) {
        return kgDataClient.batchSaveDataSetByName(addReq);
    }


}
