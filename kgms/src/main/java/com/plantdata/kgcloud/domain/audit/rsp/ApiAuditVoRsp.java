package com.plantdata.kgcloud.domain.audit.rsp;

import lombok.Data;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-16 21:00
 **/
@Data
public class ApiAuditVoRsp {
    private String name;
    private Long value;
    private String time;
}
