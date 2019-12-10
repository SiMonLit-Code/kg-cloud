package com.plantdata.kgcloud.domain.data.obtain.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphDataObtainInterface;
import com.plantdata.kgcloud.domain.common.req.PageReq;
import com.plantdata.kgcloud.domain.common.rsp.PageRsp;
import com.plantdata.kgcloud.domain.data.obtain.rsp.GraphRuleRsp;
import com.plantdata.kgcloud.sdk.KgmsClient;
import com.plantdata.kgcloud.sdk.req.GraphConfKgqlReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfKgqlRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public ApiReturn<Page<GraphConfKgqlRsp>> listByPage(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                        @ApiParam("规则类型 1gis规则 0或不传是图探索规则") @PathVariable("type") int type,
                                                        PageReq pageReq) {
        return kgmsClient.select(kgName, type, pageReq);
    }

    @ApiOperation("业务规则详情")
    @GetMapping("{id}")
    public ApiReturn<GraphConfKgqlRsp> detail(@ApiParam("规则id") @PathVariable("id") Long id) {

        return kgmsClient.detailKgql(id);
    }

    @ApiOperation("业务规则或者gis规则新增")
    @PostMapping("/kgql/{kgName}")
    public ApiReturn<GraphConfKgqlRsp> save(@PathVariable("kgName") String kgName, @RequestBody @Valid GraphConfKgqlReq req) {
        return kgmsClient.save(kgName, req);
    }

    @ApiOperation("业务规则删除")
    @DeleteMapping("{id}")
    public ApiReturn delete(@ApiParam("规则id") @PathVariable("id") Long id) {
        kgmsClient.delete(id);
        return ApiReturn.success();
    }

    @ApiOperation("业务规则修改")
    @PatchMapping("/{id}")
    public ApiReturn<GraphConfKgqlRsp> modify(@ApiParam("规则id") @PathVariable("id") Long id,
                                              @RequestBody GraphConfKgqlReq graphRuleReq) {
        return kgmsClient.update(id, graphRuleReq);
    }
}
