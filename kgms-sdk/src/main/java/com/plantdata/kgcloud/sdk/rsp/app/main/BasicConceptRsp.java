package com.plantdata.kgcloud.sdk.rsp.app.main;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 18:06
 */
@Getter
@Setter
@NoArgsConstructor
public class BasicConceptRsp {
    @ApiModelProperty("概念id")
    private Long id;
    @ApiModelProperty("概念名称")
    private String name;
    @ApiModelProperty("唯一key")
    private String key;
    @ApiModelProperty("消歧标识")
    private String meaningTag;
    @ApiModelProperty("父概念")
    private Long parentId;
    @ApiModelProperty("类型 0")
    private int type;
    @ApiModelProperty("图片")
    private String imgUrl;

}
