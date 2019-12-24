package com.plantdata.kgcloud.plantdata.req.common;

import com.plantdata.kgcloud.plantdata.bean.EntityLink;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class EntityMetaData {

    private Double score;
    private List<Tag> tags;
    private Set<EntityLink> entityLinks;
    private String fromTime;
    private String toTime;
    private String batch;
    private String source;
    private Double reliability;
    private GisBean gis;
    private String creationTime;
    private Additional additionalInfo;
    private Map<String,Object> nodeStyle;
    private Map<String,Object> labelStyle;
}
