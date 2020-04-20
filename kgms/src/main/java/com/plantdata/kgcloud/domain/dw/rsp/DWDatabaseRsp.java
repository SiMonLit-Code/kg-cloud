package com.plantdata.kgcloud.domain.dw.rsp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.plantdata.kgcloud.sdk.constant.DataType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties({"yamlContent"})
public class DWDatabaseRsp {

    private Long id;
    private String userId;
    @ApiModelProperty(value = "数仓标题")
    private String title;
    @ApiModelProperty(value = "数仓类型")
    private Integer dataType;
    @ApiModelProperty(value = "数仓数据类型 1行业标准 2PDDocument 3自定义 4RDF/OWL 5FILE 6PDD2R")
    private Integer dataFormat;
    private List<Integer> standardTemplateId;
    private List<String> addr;
    private String username;
    private String password;
    private String dbName;
    private String dataName;
    private String yamlContent;
    private String yamlFile;
    List<ModelSchemaConfigRsp> tagJson;
    private List<DWTableRsp> tables;
}
