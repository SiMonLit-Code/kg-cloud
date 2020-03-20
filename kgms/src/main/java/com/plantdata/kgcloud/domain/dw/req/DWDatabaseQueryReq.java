package com.plantdata.kgcloud.domain.dw.req;

import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("数据库查询")
public class DWDatabaseQueryReq extends BaseReq {

    private Integer dataFormat;

    private Integer createWay;

}
