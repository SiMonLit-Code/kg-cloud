package com.plantdata.kgcloud.domain.edit.req.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/12/3 18:58
 * @Description:
 */
@Data
@ApiModel("概念下实体统计模型")
public class StatisticsReq {

    @ApiModelProperty(value = "排序规则 1:升序,-1:降序")
    private Integer direction = -1;

    private Integer pos = 0;

    private Integer size = 20;
}
