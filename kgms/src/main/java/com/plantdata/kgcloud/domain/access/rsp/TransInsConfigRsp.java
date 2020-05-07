package com.plantdata.kgcloud.domain.access.rsp;

import lombok.Data;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-04-21 18:23
 **/
@Data
public class TransInsConfigRsp {

    private List<String> name;

    private List<String> meaningTag;

    private Boolean nameIsEnum;
}
