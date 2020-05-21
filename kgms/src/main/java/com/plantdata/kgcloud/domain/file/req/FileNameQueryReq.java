package com.plantdata.kgcloud.domain.file.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lp
 * @date 2020/5/21 13:24
 */
@Data
@ApiModel("创建数据库和数据表名称参数")
public class FileNameQueryReq {

    @ApiModelProperty(value = "名称")
    private String name;

}
