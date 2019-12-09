package com.plantdata.kgcloud.sdk.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/10/31 16:53
 */
@Getter
@Setter
@ApiModel("gis图探索参数")
public class GisGraphExploreReq extends GisExploreReq {

    @ApiModelProperty("查询指定的概念，格式为概念id的json数组，默认为查询全部 例：[\"2131231\",\"2131232\"]")
    private List<Long> conceptIds;
    @ApiModelProperty("查询指定的概念，格式为概念唯一标识的json数组，默认为查询全部 例：[\"2131231\",\"2131232\"]")
    private List<String> conceptKeys;
    @ApiModelProperty("属性id")
    private Integer attrId;
    @ApiModelProperty("属性唯一标识 attrId为空时生效")
    private String attrKey;
    @ApiModelProperty("allowTypes字段指定的概念是否继承 true允许继承 默认false")
    private Boolean isInherit = false;
    private Integer direction = 0;
}
