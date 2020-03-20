package com.plantdata.kgcloud.domain.access.req;

import lombok.Data;

import java.util.List;

@Data
public class ResourceReq {

    private String resourceName;

    private String resourceType;

    private List<String> outputs;

    private Object config;
}
