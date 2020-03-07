package com.plantdata.kgcloud.sdk.req;

import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "预构建模式请求")
public class PreBuilderSearchReq extends BaseReq {

    @ApiModelProperty(value = "搜索词")
    private String kw;

    @ApiModelProperty(value = "模式类型")
    private String modelType;

    @ApiModelProperty(value = "模式标签")
    private String modelTag;

    @ApiModelProperty(value = "数据库id")
    private Long databaseId;



}
