package com.plantdata.kgcloud.domain.prebuilder.rsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-05-19 15:48
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TableKtrRsp {

    private String tableName;
    private String ktr;
}

