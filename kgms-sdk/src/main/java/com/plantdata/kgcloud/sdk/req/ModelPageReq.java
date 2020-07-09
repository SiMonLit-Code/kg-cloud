package com.plantdata.kgcloud.sdk.req;

import ai.plantdata.cloud.bean.BaseReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-20 10:31
 **/
@Data
public class ModelPageReq extends BaseReq {

    @ApiModelProperty("关键词")
    private String kw;
    @ApiModelProperty("模型类型")
    private Integer modelType;
}
