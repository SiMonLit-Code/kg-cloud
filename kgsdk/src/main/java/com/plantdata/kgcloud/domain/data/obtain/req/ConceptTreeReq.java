package com.plantdata.kgcloud.domain.data.obtain.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/10/31 17:39
 */
@Getter
@Setter
@ApiModel("概念查询参数")
public class ConceptTreeReq {
    @ApiModelProperty("概念id")
    private Long conceptId;
    @ApiModelProperty("概念唯一标识")
    private String conceptKey;
}
