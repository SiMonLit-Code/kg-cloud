package com.plantdata.kgcloud.domain.data.obtain.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphDataObtainInterface;
import com.plantdata.kgcloud.sdk.EditClient;
import com.plantdata.kgcloud.sdk.req.edit.KgqlReq;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author cjw
 * @date 2020/7/7  14:56
 */
@RestController
@RequestMapping("v3/kgdata/rule/reasoning")
public class QueryController implements GraphDataObtainInterface {

    @Autowired
    private EditClient editClient;

    @ApiOperation(value = "kgql查询",notes = "KGQL查询，使用PlantData知识图谱的查询语言查询数据。")
    @PostMapping("basic/execute/kgql")
    public ApiReturn executeQl(@Valid @RequestBody KgqlReq kgqlReq) {
        return editClient.executeQl(kgqlReq);
    }
}
