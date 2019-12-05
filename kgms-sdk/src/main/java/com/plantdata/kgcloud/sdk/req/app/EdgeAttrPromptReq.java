package com.plantdata.kgcloud.sdk.req.app;


import com.plantdata.kgcloud.sdk.constant.AttrDefinitionTypeEnum;
import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Pattern;

/**
 * @author cjw 2019-11-01 13:49:25
 */
@Getter
@Setter
@ApiModel("边属性搜索参数")
public class EdgeAttrPromptReq extends BaseReq {

    @ApiModelProperty("属性定义id")
    private Integer attrId;
    @ApiModelProperty("属性定义唯一标识")
    private String attrKey;
    @ApiModelProperty("属性值id")
    private Integer seqNo;
    @ApiModelProperty("是否为保留字段：1是，0不是")
    @Pattern(regexp = "[10]")
    private Integer reserved = 0;
    @ApiModelProperty("2 对象属性 1数值属性")
    private AttrDefinitionTypeEnum dataType = AttrDefinitionTypeEnum.OBJECT;
    @ApiModelProperty("mongo语法")
    private String searchOption;
    @ApiModelProperty("关键字")
    private String kw;
    @ApiModelProperty("数值属性和日期,大小筛选,kw为空时生效,")
    private CompareFilterReq compareFilter;
}
