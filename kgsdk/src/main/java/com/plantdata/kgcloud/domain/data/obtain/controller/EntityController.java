package com.plantdata.kgcloud.domain.data.obtain.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphDataObtainInterface;
import com.plantdata.kgcloud.sdk.EditClient;
import com.plantdata.kgcloud.sdk.req.app.EntityQueryReq;
import com.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.OpenBatchResult;
import com.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.DeleteResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 10:49
 */
@RestController
@RequestMapping("v3/kgdata/entity")
public class EntityController implements GraphDataObtainInterface {

    @Autowired
    public EditClient editClient;

    @ApiOperation("实体-查询")
    @GetMapping("{kgName}/list")
    public ApiReturn<List<OpenEntityRsp>> queryEntityList(@PathVariable("kgName") String kgName,
                                                          EntityQueryReq entityQueryReq) {
        return editClient.queryEntityList(kgName, entityQueryReq.getConceptId(),
                entityQueryReq.getConceptKey(), entityQueryReq.getQuery(),
                entityQueryReq.getPage(), entityQueryReq.getSize());
    }

    @ApiOperation("实体-新增或修改")
    @PostMapping("{kgName}")
    public ApiReturn<OpenBatchResult<OpenBatchSaveEntityRsp>> batchAdd(@PathVariable("kgName") String kgName, @ApiParam("true修改") @RequestParam boolean add,
                                                                       @RequestBody List<OpenBatchSaveEntityRsp> batchEntity) {
        return editClient.saveOrUpdate(kgName, add, batchEntity);
    }

    @ApiOperation("实体-批量删除")
    @DeleteMapping("{kgName}/batch/delete")
    public ApiReturn<List<DeleteResult>> batchDeleteEntities(@PathVariable("kgName") String kgName,
                                                             @RequestParam("ids") List<Long> ids) {
        return editClient.batchDeleteEntities(kgName, ids);
    }
}
