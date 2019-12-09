package com.plantdata.kgcloud.domain.data.obtain.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphDataObtainInterface;
import com.plantdata.kgcloud.domain.common.req.PageReq;
import com.plantdata.kgcloud.domain.common.rsp.PageRsp;
import com.plantdata.kgcloud.domain.data.obtain.req.ReasoningRuleReq;
import com.plantdata.kgcloud.domain.data.obtain.rsp.ReasoningRuleRsp;
import com.plantdata.kgcloud.sdk.KgmsClient;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
 * @date 2019/11/14 12:02
 */
@RestController
@RequestMapping("kgData/reasoningRule")
public class ReasoningRuleController implements GraphDataObtainInterface {
    @Autowired
    private KgmsClient kgmsClient;

    @GetMapping("{kgName}/{type}")
    @ApiOperation("推理规则列表")
    public ApiReturn<PageRsp<ReasoningRuleRsp>> listByPage(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                           @ApiParam("规则类型 1gis规则 0或不传是图探索规则") @PathVariable("type") int type,
                                                           @Valid PageReq pageReq) {

        return ApiReturn.success(null);
    }

    @ApiOperation("推理规则详情")
    @GetMapping("{kgName}/(id)")
    public ApiReturn<ReasoningRuleRsp> detail(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                              @ApiParam("规则id") @PathVariable("id") Integer id) {
        return ApiReturn.success(null);
    }

    @ApiOperation("推理规则新增")
    @PostMapping("{kgName}")
    public ApiReturn<ReasoningRuleRsp> add(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                           @RequestBody ReasoningRuleReq reasoningRuleReq) {

        return ApiReturn.success(null);
    }

    @ApiOperation("推理规则删除")
    @DeleteMapping("{kgName}/(id)")
    public ApiReturn<ReasoningRuleRsp> delete(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                              @ApiParam("规则id") @PathVariable("id") Integer id) {
        return ApiReturn.success(null);
    }

    @ApiOperation("推理规则修改")
    @PatchMapping("{kgName}/(id)")
    public ApiReturn modify(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                            @ApiParam("规则id") @PathVariable("id") Integer id,
                            @RequestBody ReasoningRuleReq reasoningRuleReq) {
        return ApiReturn.success();
    }
}
