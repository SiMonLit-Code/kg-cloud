package com.plantdata.kgcloud.domain.access.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EtlConfigReq {

    private String taskId;

    private Long databaseId;

    private List<Long> tableIds;
}
