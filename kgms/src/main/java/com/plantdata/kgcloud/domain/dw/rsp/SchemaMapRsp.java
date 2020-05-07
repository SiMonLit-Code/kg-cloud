package com.plantdata.kgcloud.domain.dw.rsp;

import com.plantdata.kgcloud.sdk.req.DataMapReq;
import lombok.Data;

import java.util.List;

@Data
public class SchemaMapRsp {

    private String kgName;
    private String resourceName;
    private List<DataMapReq> dataMapping;
}
