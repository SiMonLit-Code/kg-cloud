package com.plantdata.kgcloud.sdk.req.app;


import com.plantdata.kgcloud.sdk.req.app.dataset.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author cjw 2019-11-01 13:49:25
 */
@Getter
@Setter
@ApiModel("边属性搜索参数")
public class EdgeAttrPromptReq extends PageReq {
    @NotNull
    @ApiModelProperty(value = "属性定义id id和key至少天一个")
    private Integer attrId;
    @ApiModelProperty("属性定义唯一标识")
    private String attrKey;
    @ApiModelProperty(value = "边属性id", required = true)
    private Integer seqNo;
    @ApiModelProperty("是否为保留字段：1是，0不是")
    @Min(0)
    @Max(1)
    private int reserved = 0;
    @ApiModelProperty("2 对象属性 1数值属性")
    @Min(1)
    @Max(2)
    private Integer dataType = 2;
    @ApiModelProperty("mongo语法")
    private String searchOption;
    @ApiModelProperty("关键字")
    private String kw;
    @ApiModelProperty("数值属性和日期,大小筛选,kw为空时生效,")
    private CompareFilterReq compareFilter;
}
