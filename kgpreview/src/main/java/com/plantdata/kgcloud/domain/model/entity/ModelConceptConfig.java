package com.plantdata.kgcloud.domain.model.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiezhenxiang 2019/7/30
 */
@Data
@ApiModel
public class ModelConceptConfig {

    /** d2r配置的实体id */
    private List<String> id;
    /** 概念名 */
    private String conceptName;
    /** 实体名称字段 */
    private String entityField;
    /** 实体属性配置 */
    List<AttrConfig> attrConfigs;
    /** 建模后的概念Id */
    private Long conceptId;

    public List<AttrConfig> getAttrConfigs() {
        return attrConfigs == null ? new ArrayList<>() : attrConfigs;
    }

    public List<String> getId() {
        return id == null ? new ArrayList<>() : id;
    }
}
