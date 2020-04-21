package com.plantdata.kgcloud.domain.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@ApiModel("数仓错误数据")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataErrStore {

    private String dbName;

    private String dbTable;

    private String fields;

    private String status;

    private String errorReason;

}
