package com.plantdata.kgcloud.domain.data.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.domain.data.req.DataStoreModifyReq;
import com.plantdata.kgcloud.domain.data.req.DataStoreScreenReq;
import com.plantdata.kgcloud.domain.data.rsp.DataStoreRsp;
import com.plantdata.kgcloud.domain.data.service.DataStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2020/3/24 17:06
 * @Description:
 */
@Api(tags = "数仓")
@RestController
@RequestMapping("/data/store")
public class DataStoreController {

    @Autowired
    private DataStoreService dataStoreService;

    @ApiOperation("数仓列表")
    @GetMapping("/list")
    public ApiReturn<BasePage<DataStoreRsp>> listDataStore(DataStoreScreenReq req) {
        return ApiReturn.success(dataStoreService.listDataStore(req));
    }

    @ApiOperation("修正数据")
    @PostMapping("/update")
    public ApiReturn updateData(@RequestBody DataStoreModifyReq modifyReq) {
        dataStoreService.updateData(modifyReq);
        return ApiReturn.success();
    }

    @ApiOperation("删除数据")
    @DeleteMapping("/{id}/delete")
    public ApiReturn deleteData(@PathVariable("id") String id) {
        dataStoreService.deleteData(id);
        return ApiReturn.success();
    }

    @ApiOperation("批量发回数仓")
    @PostMapping("/send")
    public ApiReturn sendData(@RequestBody List<String> ids) {
        dataStoreService.sendData(ids);
        return ApiReturn.success();
    }

}
