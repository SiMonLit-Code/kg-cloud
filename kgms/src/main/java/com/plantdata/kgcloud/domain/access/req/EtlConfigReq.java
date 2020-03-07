package com.plantdata.kgcloud.domain.access.req;

import lombok.Data;

import java.util.List;

@Data
public class EtlConfigReq {

    private String taskId;

    private Long databaseId;

    private List<Long> tableIds;
}
