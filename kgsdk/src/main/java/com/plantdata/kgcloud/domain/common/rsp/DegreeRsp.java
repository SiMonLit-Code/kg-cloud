package com.plantdata.kgcloud.domain.common.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/1 17:48
 */
@Getter
@Setter
@ApiModel("关系度数")
@ToString
public class DegreeRsp {

    @ApiModelProperty("出度:正向对象属性数量")
    private Integer outDegree;
    @ApiModelProperty("入度:正向对象属性数量")
    private Integer inDegree;
    @ApiModelProperty("度:双向对象属性数量")
    private Integer degree;
    @ApiModelProperty("层数")
    private Integer layer;
}
