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
public class TransRelationConfigRsp extends TransObjRsp{


    private String timeFrom;

    private String timeTo;

    private TransInsConfigRsp rela;

    private List<TransPropertyRsp> value;

    private TransInsConfigRsp valueType;

    private List<TransAttrRsp> relaAttrs;


}
