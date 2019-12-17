package com.plantdata.kgcloud.domain.edit.req.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/11/25 10:35
 * @Description:
 */
@Data
@ApiModel("数值属性值更新模型")
public class NumericalAttrValueReq {

    @NotNull
    @ApiModelProperty(value = "实体id")
    private Long entityId;

    @NotNull
    @ApiModelProperty(value = "属性id")
    private Integer attrId;

    @ApiModelProperty(value = "属性值")
    private String attrValue;

    @ApiModelProperty(value = "url类型属性值")
    private UrlAttrValue urlAttrValue;

    @Getter
    @Setter
    @ApiModel("url类型")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class UrlAttrValue {
        @ApiModelProperty(value = "url属性值名称")
        private String name;

        @ApiModelProperty(value = "url属性值链接")
        private String href;

        @ApiModelProperty(value = "url属性值缩略图路径")
        private String thumpPath;

        @ApiModelProperty(value = "url属性值类型,前端用")
        private String type;
    }
}
