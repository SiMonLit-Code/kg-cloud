package com.plantdata.kgcloud.domain.prebuilder.req;

import com.plantdata.kgcloud.domain.prebuilder.aop.BaseHandlerReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-10 16:56
 **/
@Data
@ApiModel("预构建模式分类查询")
public class PreBuilderQueryTypeReq extends BaseHandlerReq {

    @ApiModelProperty("是否模式管理列表")
    private Boolean isManage;
}
