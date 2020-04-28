package com.plantdata.kgcloud.domain.edit.rsp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author lp
 * @create 2020/4/27 22:10
 */
@Data
@Builder
public class DWFileRsp {

    private String id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("标引类型(0：文件,1：文本,2：链接)")
    private Integer indexType;

    @ApiModelProperty("关键词")
    private String keyword;

    @ApiModelProperty("简介")
    private String description;

    @ApiModelProperty("链接")
    private String url;

    @ApiModelProperty("实体文件关联信息")
    private List<EntityFileRelationRsp> relationList;

}
