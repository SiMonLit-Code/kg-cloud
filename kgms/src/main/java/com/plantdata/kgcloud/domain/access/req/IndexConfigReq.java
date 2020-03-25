package com.plantdata.kgcloud.domain.access.req;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndexConfigReq {

    private String index;

    private String type;

    private List<JSONObject> setting;
}
