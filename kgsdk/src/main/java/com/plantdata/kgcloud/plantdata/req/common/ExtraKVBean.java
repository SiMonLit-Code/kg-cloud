package com.plantdata.kgcloud.plantdata.req.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExtraKVBean {
    private String k;
    private Object v;
    private Integer attDefid;
    private Integer type;
    private Long domain;
}
