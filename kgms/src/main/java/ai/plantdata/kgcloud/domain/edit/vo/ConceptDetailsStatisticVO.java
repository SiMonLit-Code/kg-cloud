package ai.plantdata.kgcloud.domain.edit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/12/3 17:29
 * @Description:
 */
@Data
@ApiModel("概念详情统计模型")
public class ConceptDetailsStatisticVO {

    @ApiModelProperty(value = "概念id")
    private Long id;

    @ApiModelProperty(value = "概念名称")
    private String name;

    @ApiModelProperty(value = "概念下的实体总数")
    private Long total;
}
