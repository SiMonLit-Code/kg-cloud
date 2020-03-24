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

    private Integer createWay;

    private Long dwDataBaseId;

    private String queryField;

    private String cron;

    private Integer isAll;

    private Integer schedulingSwitch;

    private Date createAt;

    private Date updateAt;

}
