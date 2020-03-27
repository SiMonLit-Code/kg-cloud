package com.plantdata.kgcloud.domain.access.rsp;

import lombok.Data;

@Data
public class KtrConfigRsp {

    private String databaseName;

    private String tableIds;

    private Integer isScheduled;
}
