package com.plantdata.kgcloud.sdk.req.edit;

import lombok.Data;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-23 14:40
 **/
@Data
public class ExtraInfoReq {
    private int seqNo;
    private String name;
    private int dataType;
    private int type;
    private String objRange;
    private String dataUnit;
    private int indexed;

}
