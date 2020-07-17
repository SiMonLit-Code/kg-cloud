package ai.plantdata.kgcloud.sdk.rsp.edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: LinHo
 * @Date: 2019/11/16 18:31
 * @Description:
 */
@Data
@ApiModel("实体修改模型")
public class EntityModifyReq {

    @ApiModelProperty(required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(hidden = true, value = "0:概念,1:实体", allowableValues = "0,1")
    private Integer type = 1;

    @ApiModelProperty(required = true, value = "概念或实体名称")
    @NotEmpty
    @NotBlank
    @Length(max = 50, message = "实体名称长度不能超过50")
    private String name;

    @ApiModelProperty(value = "消歧标识")
    @Length(max = 100, message = "消歧项长度不能超过100")
    private String meaningTag;
}
