package com.plantdata.kgcloud.domain.access.req;

import lombok.Data;

import java.util.List;

@Data
public class RunTaskReq {

    private String id;

    private String type;

    private List<String> outputs;

}
