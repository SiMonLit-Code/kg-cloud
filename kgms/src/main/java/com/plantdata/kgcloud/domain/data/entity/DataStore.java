package com.plantdata.kgcloud.domain.data.entity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: LinHo
 * @Date: 2020/3/24 15:04
 * @Description:
 */
@Setter
@Getter
@ApiModel("数仓数据")
public class DataStore {

    private String dbName;

    private String dbTable;

    private String data;

    private String fields;

    private String status;

    private String errorReason;

    private String resourceName;

    private String operationType;

}
