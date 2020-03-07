package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("数仓创建数据库")
public class DWDatabaseReq {

    @ApiModelProperty("名称")
    private String title;

    @ApiModelProperty("数仓数据类型 1行业标准(需要指定使用的哪一个标准) 2pddoc 3自定义")
    private Integer dataFormat;

    @ApiModelProperty("引用的行业模板id")
    private Long standardTemplateId;

}
