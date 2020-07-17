package ai.plantdata.kgcloud.domain.graph.attr.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: LinHo
 * @Date: 2019/12/3 10:18
 * @Description:
 */
@Data
@ApiModel("属性分组创建模型")
public class AttrGroupReq {

    @ApiModelProperty(value = "属性分组名称")
    @NotEmpty(message = "属性分组名称不能为空")
    @Length(max = 100, message = "属性分组名称不能超过100")
    private String groupName;
}
