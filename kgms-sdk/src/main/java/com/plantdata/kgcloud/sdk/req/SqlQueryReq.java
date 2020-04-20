package com.plantdata.kgcloud.sdk.req;

import lombok.Data;
import com.plantdata.kgcloud.sdk.kgcompute.stat.bean.PdStatBean;

@Data
public class SqlQueryReq {
    private String dbName;
    private String tbName;
    private PdStatBean query;
}
