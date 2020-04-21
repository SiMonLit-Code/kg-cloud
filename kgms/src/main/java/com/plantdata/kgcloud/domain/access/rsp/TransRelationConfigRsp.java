package com.plantdata.kgcloud.domain.access.rsp;

import lombok.Data;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-04-21 18:22
 **/
@Data
public class TransRelationConfigRsp {

    private TransTraceRsp trace = new TransTraceRsp();

    private String timeFrom;

    private String timeTo;

    private String dataType;

    private TransInsConfigRsp entityType;

    private List<TransPropertyRsp> entity;

    private TransInsConfigRsp rela;

    private List<TransPropertyRsp> value;

    private TransInsConfigRsp valueType;

    private List<TransAttrRsp> relaAttrs;


}
