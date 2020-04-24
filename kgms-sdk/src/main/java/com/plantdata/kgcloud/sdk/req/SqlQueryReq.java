package com.plantdata.kgcloud.sdk.req;

import lombok.Data;
import com.plantdata.kgcloud.sdk.kgcompute.stat.bean.PdStatBean;

@Data
public class SqlQueryReq {
    private Integer dbId;
    private String tbName;
    private PdStatBean query;
    private String dbName;
}
