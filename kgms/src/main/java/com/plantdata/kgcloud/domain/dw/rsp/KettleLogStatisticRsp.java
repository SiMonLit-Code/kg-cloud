package com.plantdata.kgcloud.domain.dw.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

/**
 * @author Administrator
 * @Description
 * @data 2020-03-29 9:33
 **/
@ApiModel("Kettle日志统计结果")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KettleLogStatisticRsp {

    public static final KettleLogStatisticRsp EMPTY = new KettleLogStatisticRsp(null, new LinkedHashMap<>());
    @ApiModelProperty("最后一次统计日期")
    private Date lastStatisticDate;
    @ApiModelProperty("统计结果,k->日期 v")
    private LinkedHashMap<String, List<MeasureRsp>> result;

    @ApiModel("Kettle日志统计度量")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MeasureRsp {
        private String tableName;
        private Long count;
    }
}
