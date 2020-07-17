package ai.plantdata.kgcloud.domain.edit.req.induce;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @Author: LinHo
 * @Date: 2019/11/28 19:01
 * @Description:
 */
@Data
@ApiModel("属性对象化模型")
public class InduceObjectReq {

    @ApiModelProperty(value = "规约出的属性id")
    private Integer attributeId;

    @Length(max = 50, message = "长度不能超过50")
    @ApiModelProperty(value = "规约出的属性名称")
    private String attributeName;

    @ApiModelProperty(value = "对象属性的值域id，(单值)")
    private Long rangeId;
}
