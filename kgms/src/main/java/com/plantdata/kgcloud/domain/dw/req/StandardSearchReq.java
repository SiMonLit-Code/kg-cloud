package com.plantdata.kgcloud.domain.dw.req;

import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-03-26 10:50
 **/
@Data
public class StandardSearchReq extends BaseReq {

    @ApiModelProperty(value = "搜索词")
    private String kw;

    @ApiModelProperty(value = "模式类型")
    private String modelType;
}
