package com.plantdata.kgcloud.domain.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DWData dwData = (DWData) o;
        return Objects.equals(tableName, dwData.tableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName);
    }
}
