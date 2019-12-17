package com.plantdata.kgcloud.plantdata.req.common;

import lombok.Data;

import java.util.Map;

@Data
public class RelationMetaData {

    private Double score = 0.0;
    private String batch;
    private Double reliability;
    private String creationTime;
    private OriginBean origin;
    private Additional additionalInfo;
    private Map<String,Object> linkStyle;
    private Map<String,Object> labelStyle;
}
