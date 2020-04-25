package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.req.SqlQueryReq;
import com.plantdata.kgcloud.sdk.req.StatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.TableStatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.app.*;
import com.plantdata.kgcloud.sdk.req.app.algorithm.BusinessGraphRsp;
import com.plantdata.kgcloud.sdk.req.app.explore.*;
import com.plantdata.kgcloud.sdk.req.app.infobox.BatchInfoBoxReqList;
import com.plantdata.kgcloud.sdk.req.app.infobox.InfoBoxReq;
import com.plantdata.kgcloud.sdk.rsp.DW2dTableRsp;
import com.plantdata.kgcloud.sdk.rsp.DW3dTableRsp;
import com.plantdata.kgcloud.sdk.rsp.app.ComplexGraphVisualRsp;
import com.plantdata.kgcloud.sdk.rsp.app.EdgeAttributeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.PageRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.*;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisLocusAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.*;
import com.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author cx
 * @version 1.0
 * @date 2020/4/24 16:55
 */
@FeignClient(value = "kgms", path = "/dw", contextId = "DWClient")
public interface DWClient {
    /**
     * 统计数据仓库二维/按表统计
     *
     * @param
     * @return .
     */
    @PostMapping("statistic/by2dTable")
    ApiReturn<DW2dTableRsp> statisticBy2dTable(@Valid @RequestBody SqlQueryReq req);

    /**
     * 统计数据仓库三维/按表统计
     *
     * @param
     * @return .
     */
    @PostMapping("statistic/by3dTable")
    ApiReturn<DW3dTableRsp> statisticBy3dTable(@Valid @RequestBody SqlQueryReq req);

//    /**
//     * 查找所有数据库
//     *
//     * @param
//     * @return .
//     */
//    @PostMapping("statistic/by3dTable")
//    ApiReturn<List<DWTableRsp>> findAll();
}
