package com.plantdata.kgcloud.domain.graph.log.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.domain.graph.log.entity.DataLogRsp;
import com.plantdata.kgcloud.domain.graph.log.entity.ServiceLogReq;
import com.plantdata.kgcloud.domain.graph.log.entity.ServiceLogRsp;
import com.plantdata.kgcloud.domain.graph.log.service.GraphLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @author xiezhenxiang 2020/1/15
 */
@RequestMapping("/log")
@RestController
@Api(tags = "图谱日志")
public class GraphLogController {

    @Autowired
    private GraphLogService graphLogService;

    @ApiOperation("查询业务层日志")
    @PostMapping("/service/{kgName}")
    public ApiReturn<BasePage<ServiceLogRsp>> serviceLogList(@PathVariable("kgName") String kgName,
                                                             @RequestBody ServiceLogReq req) {
        BasePage<ServiceLogRsp> page = graphLogService.serviceLogList(kgName, req);
        return ApiReturn.success(page);
    }

    @GetMapping("/data/{kgName}/{batch}")
    @ApiOperation("查询数据层日志根据批次号")
    public ApiReturn<BasePage<DataLogRsp>> dataLogList(@PathVariable("kgName") String kgName,
                                                       @ApiParam("批次号") @PathVariable("batch") String batch,
                                                       BaseReq req) {
        BasePage<DataLogRsp> page = graphLogService.dataLogList(kgName, batch, req);
        return ApiReturn.success(page);
    }

    @GetMapping("/{kgName}/entity/{id}")
    @ApiOperation("查询实例编辑/概念编辑的数据层日志")
    public ApiReturn<BasePage<DataLogRsp>> entityLogList(@PathVariable("kgName") String kgName,
                                                         @ApiParam("实体ID") @PathVariable("id") Long id,
                                                         @NotNull @ApiParam("实体类型") @RequestParam("type") Integer type,
                                                         BaseReq req) {
        BasePage<DataLogRsp> page = graphLogService.entityLogList(kgName, id, type, req);
        return ApiReturn.success(page);
    }

    @GetMapping("/{kgName}/attr/{relationAttrId}/edge/define")
    @ApiOperation("查询边属性定义的数据层日志")
    public ApiReturn<BasePage<DataLogRsp>> entityLogList(@PathVariable("kgName") String kgName,
                                                         @ApiParam("关系ID") @PathVariable("relationAttrId") Integer relationAttrId,
                                                         BaseReq req) {
        BasePage<DataLogRsp> page = graphLogService.edgeAttrLogList(kgName, relationAttrId, req);
        return ApiReturn.success(page);
    }

    @GetMapping("/{kgName}/relation/entity/{entityId}")
    @ApiOperation("查询关系编辑的数据层日志")
    public ApiReturn<BasePage<DataLogRsp>> relationLogList(@PathVariable("kgName") String kgName,
                                                           @ApiParam("实体ID") @PathVariable("entityId") Long entityId,
                                                           BaseReq req) {
        BasePage<DataLogRsp> page = graphLogService.relationLogList(kgName, entityId, req);
        return ApiReturn.success(page);
    }

}
