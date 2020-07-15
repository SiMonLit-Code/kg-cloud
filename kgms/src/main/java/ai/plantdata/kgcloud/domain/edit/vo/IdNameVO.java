package ai.plantdata.kgcloud.domain.edit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: LinHo
 * @Date: 2019/11/26 17:24
 * @Description:
 */
@Data
@ApiModel("概念id和名称模型")
public class IdNameVO {

    @ApiModelProperty(value = "概念id")
    private Long id;

    @ApiModelProperty(value = "概念名称")
    @NotBlank
    private String name;
}
