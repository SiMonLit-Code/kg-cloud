package ai.plantdata.kgcloud.domain.graph.quality.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author lp
 * @date 2020/5/22 20:47
 */
@Data
@Builder
public class GraphStatistics {

    @ApiModelProperty("当前概念实体数量")
    private Long entityCount;

    @ApiModelProperty("当前概念所有实体数量(包含子概念)")
    private Long entityTotal;

    @ApiModelProperty("属性数量")
    private Integer attrDefinitionCount;

}
