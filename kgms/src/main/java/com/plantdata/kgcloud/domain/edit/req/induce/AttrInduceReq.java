package com.plantdata.kgcloud.domain.edit.req.induce;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/11/28 17:51
 * @Description:
 */
@Data
@ApiModel("属性优化推荐模型")
public class AttrInduceReq {

    @ApiModelProperty(value = "概念id")
    private Long conceptId;

    @ApiModelProperty(value = "优化类型,0:全部,1:对象化,2:公有化,3:合并")
    private Integer type;

    @ApiModelProperty(value = "阈值数量")
    private Integer number;
}
