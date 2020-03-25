package com.plantdata.kgcloud.domain.access.rsp;

import com.plantdata.kgcloud.sdk.req.DataMapReq;
import lombok.Data;

import java.util.List;

@Data
public class KgConfigRsp {

    private String kgName;

    private List<DataMapReq> dataMapping;

    private Integer isScheduled;
}
