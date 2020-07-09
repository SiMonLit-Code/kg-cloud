package com.plantdata.kgcloud.sdk.req;

import ai.plantdata.cloud.bean.BaseReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-08 12:07
 **/
@Data
public class GraphPageReq extends BaseReq {
    @ApiModelProperty("关键词")
    private String kw;
}
