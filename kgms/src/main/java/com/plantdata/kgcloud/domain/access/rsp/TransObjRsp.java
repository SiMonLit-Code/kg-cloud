package com.plantdata.kgcloud.domain.access.rsp;

import lombok.Data;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-04-22 18:07
 **/
@Data
public class TransObjRsp {

    private TransTraceRsp trace = new TransTraceRsp();

    private String dataType;

    private TransInsConfigRsp entityType;

    private List<TransPropertyRsp> entity;
}
