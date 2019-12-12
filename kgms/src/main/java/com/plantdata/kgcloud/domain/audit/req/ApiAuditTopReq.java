package com.plantdata.kgcloud.domain.audit.req;

import lombok.Data;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-12 11:45
 **/
@Data
public class ApiAuditTopReq extends ApiAuditReq {

    private Integer order;
}
