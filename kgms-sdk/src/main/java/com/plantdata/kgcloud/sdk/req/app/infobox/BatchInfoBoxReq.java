package com.plantdata.kgcloud.sdk.req.app.infobox;

import com.plantdata.kgcloud.sdk.req.app.function.AttrDefKeyReqInterface;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/4 10:10
 */
@ApiModel("知识卡片-参数(批量)")
@Getter
@Setter
public class BatchInfoBoxReq implements AttrDefKeyReqInterface {
    @ApiModelProperty(value = "实体id、概念id", required = true)
    @NonNull
    private List<Long> ids;
    @ApiModelProperty("是否读取对象属性,默认true")
    private Boolean relationAttrs = true;
    @ApiModelProperty("是否读取反向对象属性,默认true")
    private Boolean reverseRelationAttrs = true;
    @ApiModelProperty("查询指定的属性，格式为json数组格式 例：[1,2]，默认为读取全部")
    private List<Integer> allowAttrs;
    @ApiModelProperty("指定的属性的唯一标识 allowAttrs为空时生效 例：[\"key1\",\"key2\"]")
    private List<String> allowAttrsKey;

}
