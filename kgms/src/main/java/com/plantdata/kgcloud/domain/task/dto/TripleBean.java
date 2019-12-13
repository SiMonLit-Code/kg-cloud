package com.plantdata.kgcloud.domain.task.dto;

import lombok.Data;

@Data
public class TripleBean {
    private String id;
    /**
     * 0 not 1 yes
     */
    private int status;
    private NodeBean start;
    private EdgeBean edge;
    private NodeBean end;
    private String reason;

}
