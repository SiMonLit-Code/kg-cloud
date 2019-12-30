package com.plantdata.kgcloud.domain.annotation.entity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Objects;

/**
 * @author xiezhenxiang 2019/6/4
 */
@Data
@ApiModel
public class TranceBean {

    /** 标引输出字段 */
    public String field;
    /** 溯源层级 */
    public Integer level;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TranceBean that = (TranceBean) o;
        return Objects.equals(field, that.field) &&
                Objects.equals(level, that.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, level);
    }
}
