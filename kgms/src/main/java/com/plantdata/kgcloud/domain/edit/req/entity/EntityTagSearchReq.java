package com.plantdata.kgcloud.domain.edit.req.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: LinHo
 * @Date: 2019/12/12 19:22
 * @Description:
 */
@Getter
@Setter
@ApiModel("实体标签提示模型")
public class EntityTagSearchReq {
    @ApiModelProperty(value = "概念id")
    private Long conceptId;

    @ApiModelProperty(value = "标签关键字前缀搜索")
    private String kw;

    @ApiModelProperty(value = "数量")
    private Integer limit = 10;
}
