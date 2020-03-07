package com.plantdata.kgcloud.domain.dw.rsp;

import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StandardTemplateRsp {

    @ApiModelProperty("行业标准id")
    private Integer id;

    @ApiModelProperty("ktr文件")
    private String ktr;

    @ApiModelProperty("行业标准名称")
    private String name;

    @ApiModelProperty("行业标准样例文件内容")
    private String fileContent;

    @ApiModelProperty("行业模板schema")
    private List<StandardTemplateSchema> stSchemas;

    @ApiModelProperty("行业标准状态")
    private String status;

    @ApiModelProperty("打标yaml文件内容")
    private String yamlContent;

    @ApiModelProperty("yaml对应json配置")
    private String tagJson;

    @ApiModelProperty("创建时间")
    private Date createAt;

    @ApiModelProperty("更新时间")
    private Date updateAt;

}
