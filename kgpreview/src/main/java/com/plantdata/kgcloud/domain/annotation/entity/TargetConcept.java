package com.plantdata.kgcloud.domain.annotation.entity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author xiezhenxiang 2019/6/4
 */
@Data
@ApiModel
public class TargetConcept {

    /** 概念ID */
    public Long conceptId;
    /** 标引到文章的字段 */
    public String field;
}
