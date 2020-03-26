package com.plantdata.kgcloud.domain.dw.rsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: kg-cloud-kgms
 * @description: 标准模板表ktr清洗
 * @author: czj
 * @create: 2020-03-13 16:30
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TableKtrRsp {

    private String tableName;
    private String ktr;
}
