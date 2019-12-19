package com.plantdata.kgcloud.sdk.req;

import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-07 19:44
 **/
@Data
public class DataOptQueryReq extends BaseReq {

    @ApiModelProperty("查询的列")
    private String field;

    @ApiModelProperty("查询的关键词")
    private String kw;

    @ApiModelProperty("校验结果")
    private Integer resultType;

    @ApiModelProperty("创建时间范围")
    private DateTimeScope create;

    @ApiModelProperty("更新时间范围")
    private DateTimeScope update;

}
