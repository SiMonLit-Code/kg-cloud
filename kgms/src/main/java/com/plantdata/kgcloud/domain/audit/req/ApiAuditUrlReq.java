package com.plantdata.kgcloud.domain.audit.req;

import lombok.Data;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-12 11:46
 **/
@Data
public class ApiAuditUrlReq extends ApiAuditReq {
    private Integer group;
}
