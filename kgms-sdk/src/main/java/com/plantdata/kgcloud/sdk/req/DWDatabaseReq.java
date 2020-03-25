package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("数仓创建数据库")
public class DWDatabaseReq {

    @ApiModelProperty("名称")
    private String title;

    @ApiModelProperty("数仓数据类型 1行业标准 2PDDocument 3自定义 4RDF/OWL 5FILE")
    private Integer dataFormat;

    @ApiModelProperty("引用的行业模板id")
    private List<Integer> standardTemplateId;

}
