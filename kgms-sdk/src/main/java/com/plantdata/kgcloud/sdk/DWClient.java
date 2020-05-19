package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.req.DWDatabaseQueryReq;
import com.plantdata.kgcloud.sdk.req.DWTableSchedulingReq;
import com.plantdata.kgcloud.sdk.req.DataOptQueryReq;
import com.plantdata.kgcloud.sdk.rsp.DW2dTableRsp;
import com.plantdata.kgcloud.sdk.rsp.DW3dTableRsp;
import com.plantdata.kgcloud.sdk.rsp.DWDatabaseRsp;
import com.plantdata.kgcloud.sdk.rsp.DWTableRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author cx
 * @version 1.0
 * @date 2020/4/24 16:55
 */
@FeignClient(value = "kgms", path = "/dw", contextId = "DWClient")
public interface DWClient {

    /**
     * 查找所有数据库
     *
     * @param
     * @return .
     */
    @GetMapping("database/all")
    ApiReturn<List<DWDatabaseRsp>> findAll();

    /**
     * 查找所有数据库和表
     *
     * @param
     * @return .
     */
    @GetMapping("database/table/list")
    ApiReturn<List<DWDatabaseRsp>> databaseTableList();

    /**
     * 根据数仓id获得数仓名
     *
     * @param
     * @return .
     */
    @PostMapping("database/byId/{dbId}")
    DWDatabaseRsp findById(@PathVariable("dbId")String dbId);

    /**
     * 根据表名获得表详情
     *
     * @param
     * @return .
     */
    @GetMapping("database/table/{dbId}/{tableName}")
    DWTableRsp findTableByTableName(@PathVariable("dbId") String dbId, @PathVariable("tableName") String tableName);

    /**
     * 数仓-查询指定类型的数据库
     *
     * @param
     * @return .
     */
    @PostMapping("/database/list")
    ApiReturn<Page<DWDatabaseRsp>> list(@RequestBody DWDatabaseQueryReq req);

    /**
     * 数仓-查询数据库表
     *
     * @param
     * @return .
     */
    @GetMapping("/{databaseId}/table/all")
    ApiReturn<List<DWTableRsp>> findTableAll(@PathVariable("databaseId") Long databaseId);

    /**
     * 数仓-设置表调度开关
     *
     * @param
     * @return .
     */
    @PostMapping("/set/kgsearch/scheduling")
    ApiReturn setKgsearchScheduling(@Valid @RequestBody DWTableSchedulingReq req);
}
