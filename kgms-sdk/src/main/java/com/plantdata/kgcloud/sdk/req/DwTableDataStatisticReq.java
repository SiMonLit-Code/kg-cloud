package com.plantdata.kgcloud.sdk.req;

import com.plantdata.kgcloud.sdk.constant.AggregateEnum;
import com.plantdata.kgcloud.sdk.constant.SortTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @date 2020/4/14  10:04
 */
@ApiModel("数仓表数据统计参数")
@Getter
@Setter
public class DwTableDataStatisticReq {

    @ApiModelProperty(value = "前置筛选", example = "{\"data.status\":\"200\"}")
    private Map<String, Object> filterMap;
    @ApiModelProperty(value = "分组 k->别名 aggregateType COUNT计数 SUM求和 SHOW值显示", required = true,
            example = "{\"name\":{\"jsonPath\":\"data.name\",\"aggregateType\":\"COUNT\"}}")
    private Map<String, GroupReq> groupMap;
    @ApiModelProperty(value = "排序", example = "{\"ASC\":[\"data.name\"],\"DESC\":[\"data.status\"]}")
    private Map<SortTypeEnum, List<String>> sortMap;

    @ApiModel("数仓表数据统计分组参数")
    @Getter
    @Setter
    public static class GroupReq {
        @ApiModelProperty(value = "jsonPath", required = true,example = "\"data.name\"")
        private String jsonPath;
        @ApiModelProperty(value = "统计类型 默认值显示",example = "\"COUNT\"")
        private AggregateEnum aggregateType = AggregateEnum.SHOW;
    }
}
