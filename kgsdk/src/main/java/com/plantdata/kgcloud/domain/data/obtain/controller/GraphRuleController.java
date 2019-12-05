package com.plantdata.kgcloud.domain.data.obtain.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphDataObtainInterface;
import com.plantdata.kgcloud.domain.common.req.PageReq;
import com.plantdata.kgcloud.domain.common.rsp.PageRsp;
import com.plantdata.kgcloud.domain.data.obtain.req.GraphRuleReq;
import com.plantdata.kgcloud.domain.data.obtain.rsp.GraphRuleRsp;
import com.plantdata.kgcloud.sdk.KgmsClient;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/14 10:58
 */
@Slf4j
@RestController
@RequestMapping("kgData/graphRule")
public class GraphRuleController implements GraphDataObtainInterface {

    @Autowired
    private KgmsClient kgmsClient;

    @GetMapping("{kgName}/{type}")
    @ApiOperation("业务规则列表")
    public ApiReturn<PageRsp<GraphRuleRsp>> listByPage(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                       @ApiParam("规则类型 1gis规则 0或不传是图探索规则") @PathVariable("type") int type,
                                                       @Valid PageReq pageReq) {
        return ApiReturn.success(null);
    }

    @ApiOperation("业务规则详情")
    @GetMapping("{kgName}/(id)")
    public ApiReturn<GraphRuleRsp> detail(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                          @ApiParam("规则id") @PathVariable("id") Integer id) {
        return ApiReturn.success(null);
    }

    @ApiOperation("业务规则新增")
    @PostMapping("{kgName}")
    public ApiReturn<GraphRuleRsp> add(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                       @RequestBody GraphRuleReq graphRuleReq) {

        return ApiReturn.success(null);
    }

    @ApiOperation("业务规则删除")
    @DeleteMapping("{kgName}/(id)")
    public ApiReturn<GraphRuleRsp> delete(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                          @ApiParam("规则id") @PathVariable("id") Integer id) {
        return ApiReturn.success(null);
    }

    @ApiOperation("业务规则修改")
    @PatchMapping("{kgName}/(id)")
    public ApiReturn modify(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                            @ApiParam("规则id") @PathVariable("id") Integer id,
                            @RequestBody GraphRuleReq graphRuleReq) {
        return ApiReturn.success();
    }
}
