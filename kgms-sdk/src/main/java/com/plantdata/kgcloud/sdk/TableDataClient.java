package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.req.DataOptQueryReq;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author cjw
 * @date 2020/4/14  13:13
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
}
