package com.plantdata.kgcloud.plantdata.req.nlp;

import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 */
@Data
public class TagConfig {
    private String name;
    private List<ModelConfig> modelConfigList;
}
