package com.plantdata.kgcloud.sdk.rsp.app.statistic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-07 15:16
 **/
@Getter
@Setter
@ApiModel("业务算法统计Series结构")
@NoArgsConstructor
@AllArgsConstructor
public class AlgorithmStatisticeSeriesItemRsp {

    private String name;

    private List<Object> data;

    @ApiModelProperty("data中统计的实体、关系id")
    private List<List<Object>> ids;

    @ApiModelProperty("表名ids中id是实体id还是关系id 1 实体 2关系")
    private Integer type;
}
