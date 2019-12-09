package com.plantdata.kgcloud.domain.j2r.entity;


import lombok.Data;

import java.util.List;

/**
 * @author xiezhenxiang 2019/5/8
 */
@Data
public class Setting {

    public Integer dataSetId;
    public String userId;
    public String kgName;
    public String name;
    public String desc;
    public String taskType = "j2r";
    public List<ConceptConfig> config;
}
