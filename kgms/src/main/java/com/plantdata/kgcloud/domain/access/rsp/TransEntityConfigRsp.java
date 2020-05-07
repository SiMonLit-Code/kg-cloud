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
public class TransEntityConfigRsp  extends TransObjRsp{

    private String dataType;

    private List<TransAttrRsp> attrs;
}
