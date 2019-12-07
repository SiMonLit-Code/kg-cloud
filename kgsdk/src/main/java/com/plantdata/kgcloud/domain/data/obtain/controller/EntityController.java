package com.plantdata.kgcloud.domain.data.obtain.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphDataObtainInterface;
import com.plantdata.kgcloud.sdk.EditClient;
import com.plantdata.kgcloud.sdk.KgDataClient;
import com.plantdata.kgcloud.sdk.req.app.EntityQueryReq;
import com.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.DeleteResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 10:49
 */
@RestController
@RequestMapping("kgData/entity")
public class EntityController implements GraphDataObtainInterface {
    @Autowired
    public KgDataClient kgDataClient;
    @Autowired
    public EditClient editClient;

    @ApiOperation("实体查询")
    @GetMapping("{kgName}/list")
    public ApiReturn<List<OpenEntityRsp>> queryEntityList(@PathVariable("kgName") String kgName,
                                                          EntityQueryReq entityQueryReq) {
        return kgDataClient.queryEntityList(kgName, entityQueryReq);
    }

    @ApiOperation("新增或修改")
    @PostMapping("app/kgData/entity/{kgName}")
    public ApiReturn batchAdd(@PathVariable("kgName") String kgName, @ApiParam("实体不存在是否新增，默认新增") boolean add,
                              @RequestBody List<OpenBatchSaveEntityRsp> batchEntity) {
        return kgDataClient.saveOrUpdate(kgName, add, batchEntity);
    }

    @ApiOperation("批量删除实体")
    @PostMapping("entity/{kgName}/batch/delete")
    public ApiReturn<List<DeleteResult>> batchDeleteEntities(@PathVariable("kgName") String kgName,
                                                             @RequestBody List<Long> ids) {
        return editClient.batchDeleteEntities(kgName, ids);
    }
}
