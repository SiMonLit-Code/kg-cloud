package com.plantdata.kgcloud.domain.dw.rsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-04-17 17:50
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModelAttrBeanRsp {
    private String name;

    private String domain;

    private Integer dataType;

}
