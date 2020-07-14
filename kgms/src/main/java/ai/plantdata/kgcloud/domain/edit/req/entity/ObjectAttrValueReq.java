package ai.plantdata.kgcloud.domain.edit.req.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/11/25 11:16
 * @Description:
 */
@Data
@ApiModel("对象属性值添加模型")
public class ObjectAttrValueReq extends NumericalAttrValueReq {

    @ApiModelProperty(value = "关系开始时间")
    private String attrTimeFrom;

    @ApiModelProperty(value = "关系截止时间")
    private String attrTimeTo;

}
