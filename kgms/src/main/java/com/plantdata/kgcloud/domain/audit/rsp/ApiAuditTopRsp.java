package com.plantdata.kgcloud.domain.audit.rsp;

import lombok.Data;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-12 11:31
 **/
@Data
public class ApiAuditTopRsp {

    private String name;
    private Integer value;
    private Double success;
    private Double fail;
}
