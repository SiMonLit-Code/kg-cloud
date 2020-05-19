package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.req.DataOptQueryReq;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.req.DWDatabaseQueryReq;
import com.plantdata.kgcloud.sdk.req.DataOptQueryReq;
import com.plantdata.kgcloud.sdk.rsp.DWDatabaseRsp;
import com.plantdata.kgcloud.sdk.rsp.DWTableRsp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author cx
 * @version 1.0
 * @date 2020/5/15 16:55
 */
@FeignClient(value = "kgms", path = "/table/data", contextId = "TableDataClient")
public interface TableDataClient {

    /**
     * 数仓数据-分页条件查询
     *
     * @param
     * @return
     */
    @ApiOperation("数仓数据-分页条件查询-feign专用")
    @PatchMapping("/list/{databaseId}/{tableId}/feign")
    public ApiReturn<List<Object>> getDataForFeign(
            @PathVariable("databaseId") Long databaseId,
            @PathVariable("tableId") Long tableId,
            DataOptQueryReq baseReq);

     /**
     * 搜索-数仓数据-数仓表分页条件查询
     *
     * @param
     * @return .
     */
    @PostMapping("columnListSearch/{databaseId}/{tableId}")
    ApiReturn<Map<String, Object>> getData2(
            @PathVariable("tableId") Long tableId,
            @PathVariable("databaseId") Long databaseId,
            DataOptQueryReq baseReq);
}
