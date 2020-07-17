package ai.plantdata.kgcloud.domain.edit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: LinHo
 * @Date: 2019/12/3 17:18
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("图谱统计模型")
public class StatisticVO {

    @ApiModelProperty(value = "概念总数")
    private Object conceptTotal;

    @ApiModelProperty(value = "实体总数")
    private Object entityTotal;

    @ApiModelProperty(value = "数值属性总数")
    private Object numericalAttrTotal;

    @ApiModelProperty(value = "对象属性总数")
    private Object objectAttrTotal;

    @ApiModelProperty(value = "私有数值属性总数")
    private Object privateNumericalAttrTotal;

    @ApiModelProperty(value = "私有对象属性总数")
    private Object privateObjectAttrTotal;

}
