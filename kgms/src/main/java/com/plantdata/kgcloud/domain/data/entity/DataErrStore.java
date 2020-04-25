package com.plantdata.kgcloud.domain.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Setter
@Getter
@ApiModel("数仓错误数据")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataErrStore {
    private Map<String, Object> data;

    private String dbName;

    private String dbTable;

    private String resourceName;

    private String operationType;

    private String status;

    private String dbTitle;

    private Long dbId;

    private String dataName;

    private String userId;

    private String target;

    private String tableName;

    private String time_;

    private String fields;

    private String errorReason;
}
