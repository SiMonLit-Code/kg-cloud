package com.plantdata.kgcloud.domain.data.obtain.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.EditClient;
import com.plantdata.kgcloud.sdk.rsp.edit.BatchRelationRsp;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/5 14:27
 */
@RestController
@RequestMapping("kgData")
public class RelationController {

    @Autowired
    private EditClient editClient;

    @ApiOperation("批量关系新增")
    @PostMapping("relation/insert/{kgName}")
    public ApiReturn<BatchRelationRsp> importRelation(@PathVariable String kgName,
                                                      @RequestBody BatchRelationRsp relation) {

        return editClient.importRelation(kgName, relation);
    }
}
