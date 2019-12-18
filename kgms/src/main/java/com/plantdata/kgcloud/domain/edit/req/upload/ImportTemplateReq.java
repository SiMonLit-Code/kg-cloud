package com.plantdata.kgcloud.domain.edit.req.upload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/12/2 13:58
 * @Description:
 */
@Data
@ApiModel("下载导入模板模型")
public class ImportTemplateReq {

    @ApiModelProperty(value = "概念id")
    private Long conceptId;

    @ApiModelProperty(value = "属性id")
    private Integer attrId;

    @ApiModelProperty(required = true, value = "模板类型,concept|entity|relation|synonymy|number|object|specific|field")
    private String type;
}
