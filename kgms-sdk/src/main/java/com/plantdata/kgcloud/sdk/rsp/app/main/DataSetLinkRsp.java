package com.plantdata.kgcloud.sdk.rsp.app.main;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/11 14:28
 */
@Getter
@Setter
public class DataSetLinkRsp {

    @ApiModelProperty("数据集ID")
    private Integer dataSetId;
    @ApiModelProperty("数据集Title")
    private String dataSetTitle;
    @ApiModelProperty("关联的数据集")
    private List<LineRsp> links;

    @ApiModel("关联的数据集的具体项")
    @Getter
    @Setter
    private static class LineRsp {
        @ApiModelProperty("数据ID")
        private String dataId;
        @ApiModelProperty("数据Title")
        private String dataTitle;
        @ApiModelProperty("权重")
        private Double score;
        @ApiModelProperty("来源 1.手工标引 2.自动标引")
        private Integer source;
    }
}
