package com.plantdata.kgcloud.domain.dw.rsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-04-17 17:48
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModelRelationAttrBeanRsp {

    private String name;

    private Integer dataType;

}
