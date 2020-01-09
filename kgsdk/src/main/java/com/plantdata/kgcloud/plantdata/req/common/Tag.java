package com.plantdata.kgcloud.plantdata.req.common;

import lombok.Data;

@Data
public class Tag {
    private String name;
    private String source;
    private String creationTime;
    private Integer grade;

}
