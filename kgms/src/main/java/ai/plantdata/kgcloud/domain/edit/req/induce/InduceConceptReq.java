package ai.plantdata.kgcloud.domain.edit.req.induce;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/11/28 19:16
 * @Description:
 */
@Data
@ApiModel("执行概念规约模型")
public class InduceConceptReq {

    @ApiModelProperty(value = "概念id")
    private Long conceptId;

    @Length(max = 50, message = "概念名称不能超过50")
    @ApiModelProperty(value = "概念名称")
    private String conceptName;

    @ApiModelProperty(value = "父概念id")
    private Long parentId;

    @ApiModelProperty("所辖实体的数值属性查询条件")
    @NotNull

    private List<Map<Integer, List<String>>> dataAttributeValues;
    @ApiModelProperty("所辖实体的对象属性查询条件")
    @NotNull
    private List<Map<Integer, List<Long>>> objectAttributeValues;
}
