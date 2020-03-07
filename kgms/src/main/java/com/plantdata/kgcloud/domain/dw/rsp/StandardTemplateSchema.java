package com.plantdata.kgcloud.domain.dw.rsp;

import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import lombok.Data;

import java.util.List;

@Data
public class StandardTemplateSchema {

    private String tbName;

    private List<DataSetSchema> schemas;
}
