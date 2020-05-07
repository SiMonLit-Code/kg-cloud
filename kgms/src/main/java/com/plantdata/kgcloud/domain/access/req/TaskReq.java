package com.plantdata.kgcloud.domain.access.req;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TaskReq {

    private String id;

    private String type;

    private Object config;

    private List<String> outputs;

}
