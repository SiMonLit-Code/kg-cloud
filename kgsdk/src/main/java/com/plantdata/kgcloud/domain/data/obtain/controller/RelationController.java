package com.plantdata.kgcloud.domain.data.obtain.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphDataObtainInterface;
import com.plantdata.kgcloud.sdk.EditClient;
import com.plantdata.kgcloud.sdk.req.EdgeSearchReqList;
import com.plantdata.kgcloud.sdk.rsp.OpenBatchResult;
import com.plantdata.kgcloud.sdk.rsp.data.RelationUpdateReq;
import com.plantdata.kgcloud.sdk.rsp.edit.BatchRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.EdgeSearchRsp;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @ApiOperation(value = "批量关系新增",notes = "新增实体间关系及边上的数值属性。")
    @PostMapping("batch/insert/{kgName}")
    public ApiReturn<OpenBatchResult<BatchRelationRsp>> importRelation(@PathVariable("kgName") String kgName,
                                                                       @RequestBody List<BatchRelationRsp> batchRelationList) {
        return editClient.importRelation(kgName, batchRelationList);
    }

    @ApiOperation(value = "批量关系删除",notes = "删除实体间的边关系。")
    @PostMapping("batch/delete/{kgName}")
    public ApiReturn deleteRelations(@PathVariable("kgName") String kgName,
                                     @RequestBody List<String> relationIds) {
        return editClient.deleteRelations(kgName, relationIds);
    }

    @ApiOperation(value = "批量关系查询",notes = "根据指定关系类型，批量读取边关系及边上数值属性。")
    @PostMapping("search/{kgName}")
    public ApiReturn<List<EdgeSearchRsp>> batchSearchRelation(@PathVariable("kgName") String kgName, @RequestBody EdgeSearchReqList queryReq) {
        return editClient.batchSearchRelation(kgName, queryReq);
    }

    @ApiOperation(value = "批量关系更新",notes = "更新关系边上的数值属性。")
    @PutMapping("batch/update/{kgName}")
    public ApiReturn<List<RelationUpdateReq>> updateRelations(@PathVariable("kgName") String kgName, @RequestBody List<RelationUpdateReq> list) {
        return editClient.updateRelations(kgName, list);
    }
}
