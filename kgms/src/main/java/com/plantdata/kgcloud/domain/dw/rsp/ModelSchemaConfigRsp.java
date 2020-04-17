package com.plantdata.kgcloud.domain.dw.rsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModelSchemaConfigRsp {

    private String tableName;

    private Integer modelId;

    private Set<String> entity;

    private Set<ModelRelationBeanRsp> relation;

    private Set<ModelAttrBeanRsp> attr;

    private Boolean syns = false;

}

