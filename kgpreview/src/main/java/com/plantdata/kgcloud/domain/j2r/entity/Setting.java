package com.plantdata.kgcloud.domain.j2r.entity;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author xiezhenxiang 2019/5/8
 */
@Data
@ApiModel("配置参数")
public class Setting {

    public Long dataSetId;
    public String userId;
    public String kgName;
    public String name;
    public String desc;
    public String taskType = "j2r";
    public List<ConceptConfig> config;
}
