package com.plantdata.kgcloud.sdk.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/12 17:11
 */
@Getter
@Setter
@ApiModel("大小比较参数")
public class CompareFilterReq {

    @ApiModelProperty("大于")
    private String gt;
    @ApiModelProperty("小于")
    private String lt;
}
