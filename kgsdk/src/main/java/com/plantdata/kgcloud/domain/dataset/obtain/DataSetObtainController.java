package com.plantdata.kgcloud.domain.dataset.obtain;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.DataSetDataObtainInterface;
import com.plantdata.kgcloud.sdk.req.app.dataset.DataSetAddReq;
import com.plantdata.kgcloud.sdk.KgDataClient;
import com.plantdata.kgcloud.sdk.req.app.dataset.NameReadReq;
import com.plantdata.kgcloud.sdk.rsp.app.RestData;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/8 10:56
 */
@RestController
@RequestMapping("v3/dataSet/obtain")
public class DataSetObtainController implements DataSetDataObtainInterface {

    @Autowired
    private KgDataClient kgDataClient;

    @ApiOperation("读数普通数据集的数据")
    @PostMapping("common")
    public ApiReturn<RestData<Map<String, Object>>> searchDataSet(NameReadReq nameReadReq) {
        return kgDataClient.searchDataSet(nameReadReq);
    }


    @ApiOperation("读数搜索数据集的数据")
    @PostMapping("search")
    public ApiReturn<RestData<Map<String, Object>>> search(@RequestBody NameReadReq nameReadReq) {
        return kgDataClient.searchDataSet(nameReadReq);
    }

    @PostMapping("add")
    @ApiOperation("数据集导入数据")
    public ApiReturn interfaceUpload(@Valid DataSetAddReq addReq) {
        return kgDataClient.batchSaveDataSetByName(addReq);
    }


}
