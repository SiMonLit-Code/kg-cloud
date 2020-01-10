package com.plantdata.kgcloud.domain.data.obtain.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphDataObtainInterface;
import com.plantdata.kgcloud.sdk.EditClient;
import com.plantdata.kgcloud.sdk.req.EdgeSearchReq;
import com.plantdata.kgcloud.sdk.rsp.OpenBatchResult;
import com.plantdata.kgcloud.sdk.rsp.data.RelationUpdateReq;
import com.plantdata.kgcloud.sdk.rsp.edit.BatchRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.EdgeSearchRsp;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/5 14:27
 */
@RestController
@RequestMapping("v3/kgdata/relation")
public class RelationController implements GraphDataObtainInterface {

    @Autowired
    private EditClient editClient;

    @ApiOperation("关系-批量新增")
    @PostMapping("batch/insert/{kgName}")
    public ApiReturn<OpenBatchResult<BatchRelationRsp>> importRelation(@PathVariable("kgName") String kgName,
                                                                       @RequestBody List<BatchRelationRsp> batchRelationList) {
        return editClient.importRelation(kgName, batchRelationList);
    }

    @ApiOperation("关系-批量删除")
    @PostMapping("batch/delete/{kgName}")
    public ApiReturn deleteRelations(@PathVariable("kgName") String kgName,
                                     @RequestBody List<String> relationIds) {
        return editClient.deleteRelations(kgName, relationIds);
    }

    @ApiOperation("关系-批量查询")
    @PostMapping("search/{kgName}")
    public ApiReturn<List<EdgeSearchRsp>> batchSearchRelation(@PathVariable("kgName") String kgName, @RequestBody EdgeSearchReq queryReq) {
        return editClient.batchSearchRelation(kgName, queryReq);
    }

    @ApiOperation("关系-批量修改")
    @PutMapping("batch/update/{kgName}")
    public ApiReturn<List<RelationUpdateReq>> updateRelations(@PathVariable("kgName") String kgName, @RequestBody List<RelationUpdateReq> list) {
        return editClient.updateRelations(kgName, list);
    }
}
