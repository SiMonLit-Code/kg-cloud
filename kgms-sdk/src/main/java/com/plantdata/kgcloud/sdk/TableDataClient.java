package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.req.DataOptQueryReq;
import com.plantdata.kgcloud.sdk.req.DwTableDataSearchReq;
import com.plantdata.kgcloud.sdk.req.DwTableDataStatisticReq;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

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
    @ApiOperation("数仓数据-分页条件查询")
    @PatchMapping("/list/{databaseId}/{tableId}")
    public ApiReturn<Page<Map<String, Object>>> getData(
            @PathVariable("tableId") Long tableId,
            @PathVariable("databaseId") Long databaseId,
            DataOptQueryReq baseReq);
}
