package com.plantdata.kgcloud.domain.app.service.impl;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.domain.app.service.DataWarehouseStatisticService;
import com.plantdata.kgcloud.domain.dw.controller.DWController;
import com.plantdata.kgcloud.sdk.req.SqlQueryReq;
import org.springframework.stereotype.Service;
import com.plantdata.kgcloud.sdk.kgcompute.bean.chart.ChartTableBean;
import com.plantdata.kgcloud.sdk.kgcompute.stat.PdStatServiceibit;
import com.plantdata.kgcloud.sdk.kgcompute.stat.bean.PdStatBean;
import com.plantdata.kgcloud.sdk.kgcompute.stat.bean.PdStatOrderBean;
import com.plantdata.kgcloud.sdk.kgcompute.stat.bean.PdStatBaseBean;

import java.util.*;

import com.plantdata.kgcloud.sdk.rsp.DataWarehouse2dTableRsp;
import com.plantdata.kgcloud.sdk.rsp.DataWarehouse3dTableRsp;

/**
 * @author cx
 * @version 1.0
 * @date 2020/4/20 16:18
 */
@Service
public class DataWarehouseStatisticServiceImpl implements DataWarehouseStatisticService {
    @Override
    public DataWarehouse2dTableRsp statisticBy2DTable(SqlQueryReq req) {
        DWController dwController = new DWController();
        dwController.findAll();
        PdStatServiceibit pdStatService = new PdStatServiceibit();
        PdStatBean pdStatBean = req.getQuery();
        ChartTableBean ctb = (ChartTableBean)pdStatService.excute(pdStatBean,req.getDbName(),req.getTbName());
        DataWarehouse2dTableRsp table = new DataWarehouse2dTableRsp();
        table.setXAxis(new ArrayList<>());
        table.setSeries(new ArrayList<>());
        List<String> xAxis = table.getXAxis();
        List<Object> series = table.getSeries();
        if(ctb.getData()!=null && ctb.getData().size() > 0){
            for(List<Object> row : ctb.getData()){
                int counter = 0;
                for(Object obj : row){
                    if(counter == 0){
                        xAxis.add((String) obj);
                        counter++;
                    }else{
                        series.add(obj);
                    }
                }
            }
        }
        return  table;
    }

    @Override
    public DataWarehouse3dTableRsp statisticBy3DTable(SqlQueryReq req) {
        PdStatServiceibit pdStatService = new PdStatServiceibit();
        PdStatBean pdStatBean = req.getQuery();
        ChartTableBean ctb = (ChartTableBean)pdStatService.excute(pdStatBean,req.getDbName(),req.getTbName());
        DataWarehouse3dTableRsp table = new DataWarehouse3dTableRsp();
        table.setXAxis(new ArrayList<>());
        table.setYAxis(new ArrayList<>());
        table.setSeries(new ArrayList<>());
        List<String> xAxis = table.getXAxis();
        List<String> yAxis = table.getYAxis();
        List<List<Object>> series = table.getSeries();
        Set<String> xAxisNameSet = new HashSet<>();
        Set<String> yAxisNameSet = new HashSet<>();
        if(ctb.getData()!=null && ctb.getData().size() > 0){
            for(List<Object> row : ctb.getData()){
                List<Object> seriesData = new ArrayList<>();
                seriesData.add(row.get(0));
                seriesData.add(row.get(1));
                seriesData.add(row.get(2));
                series.add(seriesData);
                xAxisNameSet.add((String)row.get(0));
                yAxisNameSet.add((String)row.get(1));
            }
            for(String name: xAxisNameSet){
                xAxis.add(name);
            }
            for(String name: yAxisNameSet){
                yAxis.add(name);
            }
        }
        return  table;
    }
}
