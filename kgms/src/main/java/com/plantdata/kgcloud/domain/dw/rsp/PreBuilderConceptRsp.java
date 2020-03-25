package com.plantdata.kgcloud.domain.dw.rsp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PreBuilderConceptRsp {

    @ApiModelProperty("概念id")
    private Integer id;

    @ApiModelProperty("概念名称")
    private String name;

    @ApiModelProperty("概念meaningTag")
    private String meaningTag;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("概念图片")
    private String image;

    @ApiModelProperty("父概念id")
    private Integer parentId;

    @ApiModelProperty("概念唯一标识")
    private String conceptKey;

    @ApiModelProperty("概念属性")
    private List<PreBuilderAttrRsp> attrs;

    @ApiModelProperty("添加时间")
    private Date createAt;

    @ApiModelProperty("修改时间")
    private Date updateAt;

    @ApiModelProperty("概念映射数仓表")
    private List<String> tables;
}
