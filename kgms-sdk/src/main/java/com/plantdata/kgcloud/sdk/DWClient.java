package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.rsp.DW2dTableRsp;
import com.plantdata.kgcloud.sdk.rsp.DW3dTableRsp;
import com.plantdata.kgcloud.sdk.rsp.DWDatabaseRsp;
import com.plantdata.kgcloud.sdk.rsp.DWTableRsp;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
}
