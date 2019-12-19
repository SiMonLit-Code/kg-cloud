package com.plantdata.kgcloud.sdk.req;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-19 15:38
 **/
@Data
public class DataSetSmokeReq {

    private Map<String,Object> data;

    private List<Map<String,Object>> rules;
}
