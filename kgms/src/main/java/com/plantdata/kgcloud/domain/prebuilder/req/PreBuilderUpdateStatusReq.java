package com.plantdata.kgcloud.domain.prebuilder.req;

import com.plantdata.kgcloud.domain.prebuilder.aop.BaseHandlerReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-10 15:50
 **/
@Data
public class PreBuilderUpdateStatusReq extends BaseHandlerReq {


    @ApiModelProperty("模式id")
    private Integer modelId;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("模式类型 kg(图谱) dw(数仓)")
    private String schemaType;
}
