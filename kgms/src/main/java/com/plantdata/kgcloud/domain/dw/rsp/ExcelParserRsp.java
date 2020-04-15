package com.plantdata.kgcloud.domain.dw.rsp;

import lombok.Data;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-04-15 17:53
 **/
@Data
public class ExcelParserRsp {

    private boolean isError;
    private String errorFilePath;
    private List<ModelExcelRsp> modelExcelRspList;
}
