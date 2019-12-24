package com.plantdata.kgcloud.plantdata.req.reason;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
public class LoopBean {
    private Map<String, Integer> init;
    private Map<Integer, String> loopOperation;
    private HaltBean halt;
    private Map<Integer, String> project;

}
