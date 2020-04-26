package com.plantdata.kgcloud.plantdata.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.plantdata.utilCode.kgcompute.bean.chart.ChartTableBean;
import com.plantdata.kgcloud.plantdata.utilCode.kgcompute.stat.PdStatServiceibit;
import com.plantdata.kgcloud.plantdata.utilCode.kgcompute.stat.bean.PdStatBean;
import com.plantdata.kgcloud.sdk.DWClient;
import com.plantdata.kgcloud.sdk.rsp.DWDatabaseRsp;
import com.plantdata.kgcloud.sdk.rsp.DWStatisticTableSeries;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.plantdata.kgcloud.sdk.rsp.DW2dTableRsp;
import com.plantdata.kgcloud.sdk.rsp.DW3dTableRsp;
import com.plantdata.kgcloud.plantdata.req.dw.SqlQueryReq;

import javax.validation.Valid;
import java.util.*;

/**
 * @author cx
 */
@RestController("DWController")
@RequestMapping("sdk/dw")
public class DWController implements SdkOldApiInterface {

    @Autowired
    public DWClient dwClient;

    @ApiOperation("统计数据仓库二维/按表统计")
    @PostMapping("statistic/by2dTable")
    public ApiReturn<DW2dTableRsp> statisticBy2dTable(@Valid @RequestBody SqlQueryReq req) {
        req.setDbName(dwClient.findById(req.getDbId()+"").getDataName());
        PdStatServiceibit pdStatService = new PdStatServiceibit();
        PdStatBean pdStatBean = req.getQuery();
        ChartTableBean ctb = (ChartTableBean)pdStatService.excute(pdStatBean,req.getDbName(),req.getTbName());
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
                    if(!indexMap.containsKey(row.get(0)+"")){
                        indexMap.put((String)row.get(0),indexCounter);
                        table.getXAxis().add((String)row.get(0));
                        indexCounter++;
                    }
                    if(!seriesMap.containsKey(row.get(1)+"")){
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
        return  ApiReturn.success(table);
    }

    @ApiOperation("统计数据仓库三维/按表统计")
    @PostMapping("statistic/by3dTable")
    public ApiReturn<DW3dTableRsp> statisticBy3dTable(@Valid @RequestBody SqlQueryReq req) {
        req.setDbName(dwClient.findById(req.getDbId()+"").getDataName());
        PdStatServiceibit pdStatService = new PdStatServiceibit();
        PdStatBean pdStatBean = req.getQuery();
        ChartTableBean ctb = (ChartTableBean)pdStatService.excute(pdStatBean,req.getDbName(),req.getTbName());
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
                    if(!seriesMap.containsKey(row.get(2)+"")){
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
        }
        return  ApiReturn.success(table);
    }

    @ApiOperation("数仓-查找所有数据库")
    @GetMapping("/database/all")
    public ApiReturn<List<DWDatabaseRsp>> findAll() {
        return dwClient.findAll();
    }

    @ApiOperation("数仓-查找所有数据库与表")
    @GetMapping("/database/table/list")
    public ApiReturn<List<DWDatabaseRsp>> databaseTableList() {
        return dwClient.databaseTableList();
    }
}
