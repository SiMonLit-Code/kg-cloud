package com.plantdata.kgcloud.domain.access.rsp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-04-21 18:23
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransBaseRsp {

    private String source;

    private Integer type;
}
