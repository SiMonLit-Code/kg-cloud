package com.plantdata.kgcloud.plantdata.req.reason;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class HaltBean {
    private Integer maxTimes;
    private Map<Integer, Map<String, Object>> condition;
}
