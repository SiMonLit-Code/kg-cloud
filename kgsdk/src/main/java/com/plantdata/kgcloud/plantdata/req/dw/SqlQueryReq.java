package com.plantdata.kgcloud.plantdata.req.dw;

import lombok.Data;
import com.plantdata.kgcloud.plantdata.presto.stat.bean.PdStatBean;

@Data
public class SqlQueryReq {
    private Integer dbId;
    private String tbName;
    private PdStatBean query;
    private String dbName;
}
