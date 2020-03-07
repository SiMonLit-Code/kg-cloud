package com.plantdata.kgcloud.domain.dw.util;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class YamlRelation {

    private String domain;

    private String range;

    private String relationName;
}
