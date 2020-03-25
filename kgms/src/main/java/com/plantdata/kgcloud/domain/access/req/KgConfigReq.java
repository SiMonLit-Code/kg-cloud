package com.plantdata.kgcloud.domain.access.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KgConfigReq {

    private String taskId;
    private Integer graphMapId;

}
