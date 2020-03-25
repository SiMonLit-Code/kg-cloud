package com.plantdata.kgcloud.domain.dw.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "预构建模式")
public class PreBuilderSearchRsp {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("模式名称")
    private String name;

    @ApiModelProperty("模式状态")
    private String status;

    @ApiModelProperty("模式类型")
    private String modelType;

    @ApiModelProperty("所属用户id")
    private String userId;

    @ApiModelProperty("所属用户名")
    private String username;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("数仓id")
    private Long databaseId;

    @ApiModelProperty("权限")
    private Integer permission;

    @ApiModelProperty("关联行业标准id")
    private Long standardTemplateId;

    @ApiModelProperty("概念")
    private List<PreBuilderConceptRsp> concepts;

    @ApiModelProperty("创建时间")
    private Date createAt;

    @ApiModelProperty("更新时间")
    private Date updateAt;
}
