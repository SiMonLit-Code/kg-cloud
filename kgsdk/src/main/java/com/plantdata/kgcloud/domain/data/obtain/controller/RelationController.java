package com.plantdata.kgcloud.domain.data.obtain.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphDataObtainInterface;
import com.plantdata.kgcloud.sdk.EditClient;
import com.plantdata.kgcloud.sdk.KgDataClient;
import com.plantdata.kgcloud.sdk.rsp.data.RelationUpdateReq;
import com.plantdata.kgcloud.sdk.rsp.edit.BatchRelationRsp;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("kgData")
public class RelationController implements GraphDataObtainInterface {

    @Autowired
    private EditClient editClient;

    @ApiOperation("批量关系新增")
    @PostMapping("relation/insert/{kgName}")
    public ApiReturn<BatchRelationRsp> importRelation(@PathVariable("kgName") String kgName,
                                                      @RequestBody BatchRelationRsp relation) {

        return editClient.importRelation(kgName, relation);
    }

    @ApiOperation("批量关系删除")
    @PostMapping("attribute/{kgName}/batch/relation/delete")
    public ApiReturn deleteRelations(@PathVariable("kgName") String kgName,
                                     @RequestBody List<String> relationIds) {
        return editClient.deleteRelations(kgName, relationIds);
    }

//    @ApiOperation("批量关系查询")

    @ApiOperation("批量关系修改")
    @PatchMapping("relations/update/{kgName}")
    public ApiReturn<List<RelationUpdateReq>> updateRelations(@PathVariable("kgName") String kgName, @RequestBody List<RelationUpdateReq> list) {
        return editClient.updateRelations(kgName, list);
    }
}
