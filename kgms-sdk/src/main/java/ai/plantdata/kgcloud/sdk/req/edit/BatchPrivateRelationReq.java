package ai.plantdata.kgcloud.sdk.req.edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/25 16:29
 * @Description:
 */
@Data
@ApiModel("批量添加私有对象属性值模型")
public class BatchPrivateRelationReq {

    @NotNull
    @ApiModelProperty(value = "定义域实体id")
    private Long id;

    @NotEmpty
    @ApiModelProperty(value = "私有对象属性名称")
    @Length(max = 50, message = "长度不能超过50")
    private String attrName;

    @NotNull
    @ApiModelProperty(value = "值域实体ids")
    private List<Long> ids;
}
