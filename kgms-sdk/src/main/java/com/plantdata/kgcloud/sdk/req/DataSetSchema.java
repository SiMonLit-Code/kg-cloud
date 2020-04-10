package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-07 14:02
 **/
@Data
@ApiModel
public class DataSetSchema {

    @NotBlank
    @ApiModelProperty("列")
    @Length(min = 1, max = 20, message = "字段长度必须在1-20之间")
    private String field;

    @NotNull
    @ApiModelProperty("类型 0 mysql 1 mongo 2 es 3 pd_document")
    private Integer type;

    @NotNull
    @ApiModelProperty("字段描述")
    private String desc;

    @ApiModelProperty("是否可索引 0否  1是 ")
    private int isIndex;

    @ApiModelProperty("配置")
    private Map settings;
}
