package com.plantdata.kgcloud.domain.access.rsp;

import lombok.Data;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-04-21 18:25
 **/
@Data
public class TransPropertyRsp {

    private String property;
    private List<String> mapField;
}
