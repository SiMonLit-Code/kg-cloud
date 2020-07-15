package ai.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author cjw 2019-11-07 13:49:02
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ApiModel("数据集统计参数")
public class StatisticByDimensionalReq {

    @ApiModelProperty("数据集的查询条件")
    private String query;
    @ApiModelProperty("统计条件")
    @NotBlank
    private String aggs;
    @ApiModelProperty("返回数量")
    private Integer size = 10;
    @ApiModelProperty("返回类型0 KV ; 1 ECharts")
    @Min(0)
    @Max(1)
    private Integer returnType = 0;
}
