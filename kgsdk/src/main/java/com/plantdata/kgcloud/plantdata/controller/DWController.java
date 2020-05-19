package com.plantdata.kgcloud.plantdata.controller;

import com.alibaba.fastjson.JSONObject;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.ErrorCode;
import com.plantdata.kgcloud.constant.SdkErrorCodeEnum;
import com.plantdata.kgcloud.domain.common.module.DWStatisticInterface;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.plantdata.presto.bean.chart.ChartTableBean;
import com.plantdata.kgcloud.plantdata.presto.stat.PdStatServiceibit;
import com.plantdata.kgcloud.plantdata.presto.stat.bean.PdStatBaseBean;
import com.plantdata.kgcloud.plantdata.presto.stat.bean.PdStatBean;
import com.plantdata.kgcloud.plantdata.presto.stat.bean.PdStatFilterBean;
import com.plantdata.kgcloud.plantdata.presto.stat.bean.PdStatOrderBean;
import com.plantdata.kgcloud.sdk.DWClient;
import com.plantdata.kgcloud.sdk.TableDataClient;
import com.plantdata.kgcloud.sdk.req.DataOptQueryReq;
import com.plantdata.kgcloud.sdk.TableDataClient;
import com.plantdata.kgcloud.sdk.req.DWDatabaseQueryReq;
import com.plantdata.kgcloud.sdk.req.DWTableSchedulingReq;
import com.plantdata.kgcloud.sdk.req.DataOptQueryReq;
import com.plantdata.kgcloud.sdk.rsp.*;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import com.plantdata.kgcloud.plantdata.req.dw.SqlQueryReq;
import com.plantdata.kgcloud.plantdata.req.semantic.QaKbqaParameter;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.*;

/**
 * @author cx
 */
@RestController("DWController")
@RequestMapping("sdk/dw")
public class DWController implements DWStatisticInterface {

    @Autowired
    public DWClient dwClient;

    @Autowired
    public TableDataClient tableDataClient;

    @ApiOperation(value = "统计数据仓库(二维)", notes = "以二维表的形式统计数据仓库")
    @PostMapping("statistic/by2dTable")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "query", required = true, dataType = "string", paramType = "form", value = "输入对象")
    })
    public ApiReturn<DW2dTableRsp> statisticBy2dTable(@Valid @ApiIgnore QaKbqaParameter param) {
        JSONObject jsStr = JSONObject.parseObject(param.getQuery());
        SqlQueryReq req = JSONObject.toJavaObject(jsStr,SqlQueryReq.class);
        if(req.getQuery().getDimensions() == null
                || req.getQuery().getMeasures() == null
                || req.getQuery().getMeasures().size() != 1
                || (req.getQuery().getDimensions().size() != 1
                        && req.getQuery().getDimensions().size() != 2)){
            throw BizException.of(SdkErrorCodeEnum.JSON_NOT_FIT);
        }
        DWDatabaseRsp dataBase = dwClient.findById(req.getDbId()+"");
        DWTableRsp tableDetail = dwClient.findTableByTableName(String.valueOf(req.getDbId()),req.getTbName());
        if(dataBase == null){
            throw BizException.of(SdkErrorCodeEnum.DB_NOT_EXIST);
        }else if(tableDetail.getCreateWay() == 2 || (tableDetail.getCreateWay() == 1 && tableDetail.getIsWriteDW() != null && tableDetail.getIsWriteDW() ==1 )){
        }else{
            throw BizException.of(SdkErrorCodeEnum.REMOTE_TABLE_NOT_SUPPORTED);
        }
        escape(req);
        req.setDbName(dataBase.getDataName());
        PdStatServiceibit pdStatService = new PdStatServiceibit();
        PdStatBean pdStatBean = req.getQuery();
        ChartTableBean ctb = null;
        try {
            ctb = (ChartTableBean) pdStatService.excute(pdStatBean, req.getDbName(), req.getTbName());
        }catch(Exception e){
            throw new BizException(130004,"message: "+e.getMessage()+" detail: "+e.getCause().getMessage());
        }
        DW2dTableRsp table = new DW2dTableRsp();
        table.setXAxis(new ArrayList<>());
        table.setSeries(new ArrayList<>());
        List<String> xAxis = table.getXAxis();
        if(ctb != null && ctb.getData()!=null && ctb.getData().size() > 0){
            if(req.getQuery().getDimensions().size()==1) {
                DWStatisticTableSeries s = new DWStatisticTableSeries();
                table.getSeries().add(s);
                s.setName("分组1");
                s.setData(new ArrayList<>());
                for (List<Object> row : ctb.getData()) {
                    xAxis.add((String) row.get(0));
                    s.getData().add(row.get(1));
                }
            }else if(req.getQuery().getDimensions().size()==2) {
                Map<String,Integer> seriesMap = new HashMap<>();
                Map<String,Integer> indexMap = new HashMap<>();
                int indexCounter = 0;
                int seriesCounter = 0;
                for (List<Object> row : ctb.getData()) {
                    if(!indexMap.containsKey(row.get(0))){
                        indexMap.put((String)row.get(0),indexCounter);
                        table.getXAxis().add((String)row.get(0));
                        indexCounter++;
                    }
                    if(!seriesMap.containsKey(row.get(1))){
                        seriesMap.put((String)row.get(1),seriesCounter);
                        seriesCounter++;
                    }
                }
                for(String seriesKey : seriesMap.keySet()){
                    DWStatisticTableSeries s = new DWStatisticTableSeries();
                    s.setName(seriesKey);
                    s.setData(new ArrayList<Object>());
                    table.getSeries().add(s);
                    for(String indexKey : indexMap.keySet()){
                        s.getData().add(null);
                    }
                }
                for (List<Object> row : ctb.getData()) {
                    DWStatisticTableSeries series = (DWStatisticTableSeries)table.getSeries().get(seriesMap.get(row.get(1)));
                    series.getData().set(indexMap.get(row.get(0)),row.get(2));
                }
            }
        }
        else if(ctb == null){
            throw BizException.of(SdkErrorCodeEnum.JSON_NOT_FIT);
        }
        return  ApiReturn.success(table);
    }

    @ApiOperation(value = "统计数据仓库(三维)", notes = "以三维表的形式统计数据仓库")
    @PostMapping("statistic/by3dTable")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "query", required = true, dataType = "string", paramType = "form", value = "输入对象")
    })
    public ApiReturn<DW3dTableRsp> statisticBy3dTable(@Valid @ApiIgnore QaKbqaParameter param) {
        JSONObject jsStr = JSONObject.parseObject(param.getQuery());
        SqlQueryReq req = JSONObject.toJavaObject(jsStr,SqlQueryReq.class);
        if(req.getQuery().getDimensions() == null
                || req.getQuery().getMeasures() == null
                || req.getQuery().getMeasures().size() != 1
                || (req.getQuery().getDimensions().size() != 2
                && req.getQuery().getDimensions().size() != 3)){
            throw BizException.of(SdkErrorCodeEnum.JSON_NOT_FIT);
        }
        DWDatabaseRsp dataBase = dwClient.findById(req.getDbId()+"");
        DWTableRsp tableDetail = dwClient.findTableByTableName(String.valueOf(req.getDbId()),req.getTbName());
        if(dataBase == null){
            throw BizException.of(SdkErrorCodeEnum.DB_NOT_EXIST);
        }else if(tableDetail.getCreateWay() == 2 || (tableDetail.getCreateWay() == 1 && tableDetail.getIsWriteDW() != null && tableDetail.getIsWriteDW() ==1 )){
        }else{
            throw BizException.of(SdkErrorCodeEnum.REMOTE_TABLE_NOT_SUPPORTED);
        }
        escape(req);
        req.setDbName(dataBase.getDataName());
        PdStatServiceibit pdStatService = new PdStatServiceibit();
        PdStatBean pdStatBean = req.getQuery();
        ChartTableBean ctb = null;
        try {
            ctb = (ChartTableBean) pdStatService.excute(pdStatBean, req.getDbName(), req.getTbName());
        }catch(Exception e){
            throw new BizException(130004,"message: "+e.getMessage()+" detail: "+e.getCause().getMessage());
        }
        DW3dTableRsp table = new DW3dTableRsp();
        table.setXAxis(new ArrayList<>());
        table.setYAxis(new ArrayList<>());
        table.setSeries(new ArrayList<>());
        List<String> xAxis = table.getXAxis();
        List<String> yAxis = table.getYAxis();
        Set<String> xAxisNameSet = new HashSet<>();
        Set<String> yAxisNameSet = new HashSet<>();
        if(ctb != null && ctb.getData()!=null && ctb.getData().size() > 0){
            if(req.getQuery().getDimensions().size()==2) {
                DWStatisticTableSeries s = new DWStatisticTableSeries();
                table.getSeries().add(s);
                s.setName("分组1");
                s.setData(new ArrayList<>());
                for (List<Object> row : ctb.getData()) {
                    List<Object> seriesData = new ArrayList<>();
                    seriesData.add(row.get(0));
                    seriesData.add(row.get(1));
                    seriesData.add(row.get(2));
                    s.getData().add(seriesData);
                    xAxisNameSet.add((String) row.get(0));
                    yAxisNameSet.add((String) row.get(1));
                }
                for (String name : xAxisNameSet) {
                    xAxis.add(name);
                }
                for (String name : yAxisNameSet) {
                    yAxis.add(name);
                }
            }else if(req.getQuery().getDimensions().size()==3) {
                Map<String,Integer> seriesMap = new HashMap<>();
                int counter = 0;
                for (List<Object> row : ctb.getData()) {
                    if(!seriesMap.containsKey(row.get(2))){
                        seriesMap.put((String)row.get(2),counter);
                        DWStatisticTableSeries s = new DWStatisticTableSeries();
                        s.setName((String)row.get(2));
                        s.setData(new ArrayList<>());
                        table.getSeries().add(s);
                        counter++;
                    }
                }
                for (List<Object> row : ctb.getData()) {
                    List<Object> seriesData = new ArrayList<>();
                    seriesData.add(row.get(0));
                    seriesData.add(row.get(1));
                    seriesData.add(row.get(3));
                    table.getSeries().get(seriesMap.get(row.get(2))).getData().add(seriesData);
                    xAxisNameSet.add((String) row.get(0));
                    yAxisNameSet.add((String) row.get(1));
                }
                for (String name : xAxisNameSet) {
                    xAxis.add(name);
                }
                for (String name : yAxisNameSet) {
                    yAxis.add(name);
                }
            }
        }else if(ctb == null){
            throw BizException.of(SdkErrorCodeEnum.JSON_NOT_FIT);
        }
        return  ApiReturn.success(table);
    }

    @ApiOperation(value = "数仓-查找所有数据库", notes = "查找用户创建的所有数仓数据库")
    @GetMapping("/database/list")
    public ApiReturn<List<DWDatabaseRsp>> findAll() {
        return dwClient.findAll();
    }

//    @ApiOperation(value = "数仓-查找所有数据库与表", notes = "查找用户创建的所有数仓数据库与表")
//    @GetMapping("/databaseAndTable/list")
//    public ApiReturn<List<DWDatabaseRsp>> databaseTableList() {
//        return dwClient.databaseTableList();
//    }

    public void escape(SqlQueryReq req){
        if(req != null && req.getQuery() != null){
            List<PdStatBaseBean> dimensions = req.getQuery().getDimensions();
            List<PdStatBaseBean> measures = req.getQuery().getMeasures();
            List<PdStatFilterBean> filters = req.getQuery().getFilters();
            List<PdStatOrderBean> orders = req.getQuery().getOrders();
//            if(dimensions != null){
//                for(PdStatBaseBean dimension : dimensions){
//                    //dimension.setName("'"+dimension.getName()+"'");
//                }
//            }
//            if(measures != null){
//                for(PdStatBaseBean measure : measures){
//                    measure.setName("'"+measure.getName()+"'");
//                }
//            }
//            if(filters != null){
//                for(PdStatBaseBean filter : filters){
//                    filter.setName("'"+filter.getName()+"'");
//                }
//            }
//            if(orders != null){
//                for(PdStatBaseBean order : orders){
//                    order.setName("'"+order.getName()+"'");
//                }
//            }
        }
    }

    @ApiOperation(value = "数仓-查找所有数据库与表", notes = "查找所有数据库与表")
    @GetMapping("/database/table/list")
    public ApiReturn<List<DWDatabaseRsp>> databaseTableList(){
        return dwClient.databaseTableList();
    }

    @ApiOperation(value = "数仓-查询数据库表", notes = "查询数据库表")
    @GetMapping("/{databaseId}/table/all")
    public ApiReturn<List<DWTableRsp>> findTableAll(@PathVariable("databaseId") Long databaseId){
        return dwClient.findTableAll(databaseId);
    }

    @ApiOperation("搜索-数仓数据-分页条件查询")
    @PostMapping("/table/data/columnListSearch/{databaseId}/{tableId}")
    public ApiReturn<Map<String, Object>> getData2(
            @PathVariable("tableId") Long tableId,
            @PathVariable("databaseId") Long databaseId,
            DataOptQueryReq baseReq) {
        return tableDataClient.getData2(tableId, databaseId, baseReq);
    }

    @ApiOperation("搜索-数仓-设置表调度开关")
    @PostMapping("/set/kgsearch/scheduling")
    public ApiReturn setKgsearchScheduling(@Valid @RequestBody DWTableSchedulingReq req) {
        return dwClient.setKgsearchScheduling(req);
    }

    @ApiOperation(value = "数仓数据-分页条件查询", notes = "分页条件查询")
    @PatchMapping("/list/{databaseId}/{tableId}")
    public ApiReturn<Page<Map<String, Object>>> getData(
            @PathVariable("tableId") Long tableId,
            @PathVariable("databaseId") Long databaseId,
            DataOptQueryReq baseReq) {
        ApiReturn<List<Object>> apiReturn = tableDataClient.getDataForFeign(databaseId, tableId, baseReq);
        if(apiReturn.getErrCode() != 200){
            return ApiReturn.fail(apiReturn.getErrCode(),apiReturn.getMessage());
        }
        List<Object> arguments = apiReturn.getData();
        PageRequest pageable = PageRequest.of((Integer)arguments.get(0), (Integer)arguments.get(1));
        return ApiReturn.success(new PageImpl<>((List<Map<String, Object>>)arguments.get(2), pageable, Long.valueOf((String)arguments.get(3))));
    }

}
