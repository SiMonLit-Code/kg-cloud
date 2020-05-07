package com.plantdata.kgcloud.domain.dw.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YamlColumn {

    private String columnName;

    private String conceptOrRelationName;

    private Boolean isRelationAttr;

    private String attrName;

    private String type;


}
