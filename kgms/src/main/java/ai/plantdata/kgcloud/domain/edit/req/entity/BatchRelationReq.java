package ai.plantdata.kgcloud.domain.edit.req.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/25 16:19
 * @Description:
 */
@Data
@ApiModel("批量添加对象属性值模型")
public class BatchRelationReq {

    @NotNull
    @ApiModelProperty(value = "定义域实体id")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "关系id")
    private Integer attrId;

    @NotNull
    @ApiModelProperty(value = "值域实体ids")
    private List<Long> ids;

}
