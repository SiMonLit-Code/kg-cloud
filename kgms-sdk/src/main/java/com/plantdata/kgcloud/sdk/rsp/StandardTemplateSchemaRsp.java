package com.plantdata.kgcloud.sdk.rsp;

import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StandardTemplateSchemaRsp {

    private String tableName;

    private String title;

    private List<DataSetSchema> schemas;


}

