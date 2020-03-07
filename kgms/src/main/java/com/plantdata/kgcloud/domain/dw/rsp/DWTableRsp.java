package com.plantdata.kgcloud.domain.dw.rsp;

import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DWTableRsp {

    private Long id;

    private String title;

    private String tbName;

    private String tableName;

    private List<String> fields;

    private List<DataSetSchema> schema;

    private String mapper;

    private String createType;

    private Long dwDataBaseId;

    private String queryField;

    private String cron;

    private Integer isAll;

    private Date createAt;

    private Date updateAt;

}
