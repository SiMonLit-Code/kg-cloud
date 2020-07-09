package com.plantdata.kgcloud.sdk.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

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
    private List<List<String>> xAxis = new ArrayList<>();
    private List<Map<String, Object>> series = new ArrayList<>();

    public void addData2xAxis(List<String> data) {
        if(CollectionUtils.isEmpty(this.xAxis)){
            this.xAxis= new ArrayList<>();
        }
        this.xAxis.add(data);
    }

    public void addData2Series(Map<String, Object> map) {
        if(CollectionUtils.isEmpty(this.series)){
            this.series= new ArrayList<>();
        }
        this.series.add(map);
    }

    public void addData2Series(String name, List data) {
        Map<String, Object> m = new HashMap<>(2);
        m.put("name", name);
        m.put("data", data);
        addData2Series(m);
    }

    public void addData2Series(String k, Long v) {
        Map<String, Object> m = new HashMap<>(2);
        m.put("name", k);
        m.put("value", v);
        addData2Series(m);
    }
}
