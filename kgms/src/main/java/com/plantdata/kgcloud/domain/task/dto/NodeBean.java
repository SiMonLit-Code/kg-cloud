package com.plantdata.kgcloud.domain.task.dto;

import lombok.Data;

@Data
public class NodeBean {
    /**
     * 0 entity 1 literal
     */

    private int type;
    private Long id;
    private Long conceptId;
    private String name;
    private int valueType;
    private Object value;

}
