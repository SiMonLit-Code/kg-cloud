package com.plantdata.kgcloud.sdk.rsp;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: cx
 * @create: 2020-04-17 17:23
 **/
@Data
public class DataWarehouse3dTableRsp {
    private List<String> xAxis;
    private List<String> yAxis;
    private List<List<Object>> series;
}
