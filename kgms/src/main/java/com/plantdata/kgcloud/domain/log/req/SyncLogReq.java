package com.plantdata.kgcloud.domain.log.req;

import com.plantdata.kgcloud.sdk.req.app.PageReq;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-13 21:58
 **/
@Data
public class SyncLogReq extends PageReq {

    private String dbName;

    private String tbName;

    private String kgName;
}
