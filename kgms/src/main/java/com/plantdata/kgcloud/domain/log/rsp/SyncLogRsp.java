package com.plantdata.kgcloud.domain.log.rsp;

import lombok.Data;

import java.util.Map;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-13 21:58
 **/
@Data
public class SyncLogRsp {

    private String dbName;

    private String tbName;

    private String id;

    private Map<String,Object> value;
}
