package com.plantdata.kgcloud.domain.data.obtain.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/9 14:40
 */
@Getter
@Setter
@ApiModel("属性查询条件")
public class AttributeQueryReq {
    @ApiModelProperty("概念id conceptId,conceptKey 二选一")
    private Long conceptId;
    @ApiModelProperty("概念唯一标识")
    private Long conceptKey;
    private Boolean inherit = true;
}
