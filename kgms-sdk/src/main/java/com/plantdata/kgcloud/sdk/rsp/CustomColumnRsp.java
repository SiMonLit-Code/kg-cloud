package com.plantdata.kgcloud.sdk.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-04-20 18:48
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("自定义打标-字段")
public class CustomColumnRsp {

    @ApiModelProperty("字段名")
    private String name;

    @ApiModelProperty("定义")
    private String tag;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("描述")
    private String comment;

}
