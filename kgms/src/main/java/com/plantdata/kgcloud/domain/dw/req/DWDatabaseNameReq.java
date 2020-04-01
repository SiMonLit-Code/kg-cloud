package com.plantdata.kgcloud.domain.dw.req;

import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("数据库改名")
public class DWDatabaseNameReq{

    private Long dataBaseId;

    private String name;

}
