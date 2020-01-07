package com.plantdata.kgcloud.sdk.rsp.app.sparql;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SparQlNodeRsp {

    private String key;
    private String value;
    private String type;
    private String id;
}
