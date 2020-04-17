package com.plantdata.kgcloud.domain.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@ApiModel("数仓数据-错误状态")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DWData {


    private String tableName;


}
