package com.plantdata.kgcloud.sdk.req.app.explore.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/13 16:06
 */
@Getter
@Setter
public class BasicStatisticReq {
    @ApiModelProperty("key 为唯一标识，用于区分多组结果")
    private String key;
    @ApiModelProperty("概念id")
    private Long conceptId;
    @ApiModelProperty("属性id")
    private List<Integer> attrIdList;
}
