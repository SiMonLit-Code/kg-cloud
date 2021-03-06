package ai.plantdata.kgcloud.domain.graph.attr.rsp;

import ai.plantdata.kgcloud.domain.edit.req.attr.AttrTemplateReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Data
@ApiModel("属性模板模型")
public class GraphAttrTemplateRsp {

    @ApiModelProperty(value = "属性模板id")
    private Long id;

    @ApiModelProperty(value = "概念名称")
    private String name;

    @ApiModelProperty(value = "属性模板简介")
    private String abs;

    @ApiModelProperty(value = "属性配置")
    private List<AttrTemplateReq> config;
}
