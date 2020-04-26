package com.plantdata.kgcloud.sdk.kgcompute.bean.chart;

import java.util.List;

public class Chart2DBean {

    /**
     1维参考:
     {"xAxis":{"data":["Mon","Tue","Wed","Thu","Fri","Sa","Sun"]},"series":[{"name":"分组1","data":[820,932,901,934,1290,1330,1320]}]}
     2维参考:
     {"xAxis":{"data":["Mon","Tue","Wed","Thu","Fri","Sa","Sun"]},"series":[{"name":"分组1","data":[820,932,901,934,1290,1330,1320]},{"name":"分组2","data":[820,932,901,934,1290,1330,1320]}]}
     */

    private ChartData xAxis;
    private List<ChartData> series;

    public void setxAxis(String name, List<Object> data){
        xAxis = new ChartData(name, data);
    }

    public void addSeries(String name, List<Object> data){
        xAxis = new ChartData(name, data);
    }

    class ChartData{

        private String name;
        private List<Object> data;

        ChartData(String name, List<Object> data){
            this.name = name;
            this.data = data;
        }

    }

}
