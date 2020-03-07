package com.plantdata.kgcloud.domain.access.rsp;

import lombok.Data;

import java.util.List;

@Data
public class EtlConfigRsp {

    private Long databaseId;

    private List<Long> tableIds;
}
