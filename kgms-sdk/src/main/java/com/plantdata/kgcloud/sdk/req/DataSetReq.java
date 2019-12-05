package com.plantdata.kgcloud.sdk.req;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-06 10:38
 **/
@Data
public class DataSetReq {
    private Long folderId;
    private String title;
    private Integer dataType;
    private Integer createType;
    private String createWay;
    @Valid
    private List<DataSetSchema> cols;
}
