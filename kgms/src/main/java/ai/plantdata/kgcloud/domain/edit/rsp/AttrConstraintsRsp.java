package ai.plantdata.kgcloud.domain.edit.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/12/4 15:34
 * @Description:
 */
@Data
@ApiModel("不满足属性约束结果模型")
public class AttrConstraintsRsp {

    @ApiModelProperty(value = "tripleId")
    private String tripleId;

    @ApiModelProperty(value = "定义域实体名称")
    private String entityName;

    @ApiModelProperty(value = "属性名称")
    private String attrName;

    @ApiModelProperty(value = "属性值")
    private Object attrValue;
}
