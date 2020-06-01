package com.plantdata.kgcloud.sdk.rsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-04-24 10:46
        **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModelRangeRsp {

    private Long range;

    private String rangeName;

    private String meaningTag;
}
