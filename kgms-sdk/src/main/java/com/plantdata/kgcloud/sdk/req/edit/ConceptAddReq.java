package com.plantdata.kgcloud.sdk.req.edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/20 9:56
 */
@ApiModel("新增概念参数")
@Getter
@Setter
public class ConceptAddReq {

    @ApiModelProperty(value = "所属概念ID", required = true)
    @NotNull
    private Long parentId;

    @ApiModelProperty(required = true, value = "概念或实体名称")
    @NotEmpty
    @Length(max = 50)
    private String name;
    @ApiModelProperty(value = "概念key")
    private String key;

    @ApiModelProperty(value = "消歧标识")
    private String meaningTag;
}
