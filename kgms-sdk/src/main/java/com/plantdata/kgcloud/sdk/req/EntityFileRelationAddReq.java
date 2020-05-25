package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author lp
 * @date 2020/5/21 20:34
 */
@ApiModel("标引参数")
@Data
public class EntityFileRelationAddReq {

    @NotNull
    @ApiModelProperty("标引类型(0：文件标引，1：文本标引，3：链接标引)")
    private Integer indexType;

    @NotEmpty
    @ApiModelProperty("实体ids")
    private List<Long> entityIds;

    @ApiModelProperty("文件id(文件标引)")
    private String fileId;

    @ApiModelProperty("标题(文本、链接标引)")
    private String title;

    @ApiModelProperty("关键词")
    private String keyword;

    @ApiModelProperty("简介(文本标引)")
    private String description;

    @ApiModelProperty("链接(链接标引)")
    private String url;

}
