package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class DataSetUpdateReq {

    @ApiModelProperty("保存的列")
    private List<String> fields;
    @Valid
    @ApiModelProperty
    private List<DataSetSchema> schema;
}
