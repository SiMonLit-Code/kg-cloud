package com.plantdata.kgcloud.domain.access.req;

import lombok.Data;

@Data
public class TableConfigReq {

    private String tableName;

    private String cron;

    private String field;
}
