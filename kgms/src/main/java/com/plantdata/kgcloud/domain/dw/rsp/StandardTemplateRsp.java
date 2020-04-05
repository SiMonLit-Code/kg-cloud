package com.plantdata.kgcloud.domain.dw.rsp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties({"schemas","ktr"})
public class StandardTemplateRsp {

    @ApiModelProperty("行业标准id")
    private Integer id;

    @ApiModelProperty("行业标准名称")
    private String name;

    @ApiModelProperty("行业类别")
    private String modelType;

    @ApiModelProperty("行业标准样例文件内容")
    private String fileContent;

    @ApiModelProperty("行业标准描述")
    private String description;

    @ApiModelProperty("行业模板schema")
    private List<StandardTemplateSchema> schemas;

    @ApiModelProperty("标注对应json配置")
    private List<ModelSchemaConfigRsp> tagJson;

    @ApiModelProperty("表对应的ktr文件")
    private List<TableKtrRsp> ktr;

    @ApiModelProperty("状态 1发布 0禁用")
    private String status;

    @ApiModelProperty("创建时间")
    private Date createAt;

    @ApiModelProperty("更新时间")
    private Date updateAt;

}
