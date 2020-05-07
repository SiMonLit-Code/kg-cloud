package com.plantdata.kgcloud.domain.dw.req;

import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RemoteTableAddReq {

    @ApiModelProperty("表名称")
    private String tbName;

    @ApiModelProperty("映射已有表的名称")
    private String tableName;

    @ApiModelProperty("映射表的模式id")
    private Integer modelId;

    @ApiModelProperty("PDDOC|PDD2R 特有 指定数据存储的字段")
    private String field;

    @ApiModelProperty("字段schema")
    private DataSetSchema dataSetSchema;
}
