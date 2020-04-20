package com.plantdata.kgcloud.sdk.rsp;

import com.plantdata.kgcloud.sdk.req.AnnotationConf;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: cx
 * @create: 2020-04-17 17:23
 **/
@Data
public class DataWarehouse2dTableRsp {
    private List<String> xAxis;
    private List<Object> series;
}
