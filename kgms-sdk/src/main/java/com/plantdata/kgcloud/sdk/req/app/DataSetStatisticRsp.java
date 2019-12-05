package com.plantdata.kgcloud.sdk.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cjw 2019-11-07 17:59:27
 */
@Getter
@Setter
@ApiModel("数据集统计数据")
public class DataSetStatisticRsp {

    @ApiModelProperty("x轴")
    private List<List<String>> xAxis;
    private List<Map<String, Object>> series = new ArrayList<>();

    public void addData2xAxis(List<String> data) {
        this.xAxis.add(data);
    }

    public void addData2Series(String name, List data) {
        Map<String, Object> m = new HashMap<>(2);
        m.put("name", name);
        m.put("data", data);
        this.series.add(m);
    }

    public void addData2Series(String k, Long v) {
        Map<String, Object> m = new HashMap<>(2);
        m.put("name", k);
        m.put("value", v);
        this.series.add(m);
    }
}
