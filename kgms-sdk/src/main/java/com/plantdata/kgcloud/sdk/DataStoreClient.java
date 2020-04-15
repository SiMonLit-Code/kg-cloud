package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.req.DwTableDataSearchReq;
import com.plantdata.kgcloud.sdk.req.DwTableDataStatisticReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @date 2020/4/14  13:13
 */
@FeignClient(value = "kgms", path = "/", contextId = "dataStoreClient")
public interface DataStoreClient {
    /**
     * 数仓表下拉提示
     *
     * @param dataStoreId  数仓id
     * @param tableId      .
     * @param statisticReq
     * @return
     */
    @PostMapping("table/data/statistic/{dataStoreId}/{tableId}")
    ApiReturn<List<Map<String, Object>>> dataSoreStatistic(@PathVariable("dataStoreId") long dataStoreId,
                                                           @PathVariable("tableId") long tableId,
                                                           @RequestBody DwTableDataStatisticReq statisticReq);

    /**
     * 数仓表下拉提示
     *
     * @param dataStoreId 数仓id
     * @param tableId     .
     * @param searchReq
     * @return
     */
    @GetMapping("table/data/search/{dataStoreId}/{tableId}")
    ApiReturn<List<Map<String, Object>>> search(@PathVariable("dataStoreId") long dataStoreId,
                                                @PathVariable("tableId") long tableId, @RequestBody DwTableDataSearchReq searchReq);
}
