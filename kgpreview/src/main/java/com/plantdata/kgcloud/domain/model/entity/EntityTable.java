package com.plantdata.kgcloud.domain.model.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiezhenxiang 2019/7/11
 */
@Data
public class EntityTable {


    /** 表名 */
    private String tableName;
    /** 概念配置 */
    List<ModelConceptConfig> conceptConfigs;
    /** 概念之间的关系配置 */
    List<RelationConfig> relationConfigs;

    public List<RelationConfig> getRelationConfigs() {
        return relationConfigs == null ? new ArrayList<>() : relationConfigs;
    }

    public List<ModelConceptConfig> getConceptConfigs() {
        return conceptConfigs == null ? new ArrayList<>() : conceptConfigs;
    }
}
