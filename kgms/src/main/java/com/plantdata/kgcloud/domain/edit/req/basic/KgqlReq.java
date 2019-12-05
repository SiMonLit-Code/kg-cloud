package com.plantdata.kgcloud.domain.edit.req.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/12/4 18:47
 * @Description:
 */
@Data
@ApiModel("kgql执行模型")
public class KgqlReq {
    @ApiModelProperty("查询语句")
    private String query;
}
