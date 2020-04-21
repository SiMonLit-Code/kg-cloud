package com.plantdata.kgcloud.domain.access.rsp;

import lombok.Data;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-04-21 18:27
 **/
@Data
public class TransTraceRsp  {

    private TransBaseRsp reliability = new TransBaseRsp("",1);
    private TransBaseRsp source = new TransBaseRsp("",2);
}
