package ai.plantdata.kgcloud.domain.graph.quality.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author: LinHo
 * @Date: 2020/3/21 14:27
 * @Description:
 */
@Setter
@Getter
@ApiModel("概念本身实体、属性统计信息")
public class GraphQualityRsp {

    @ApiModelProperty("质量id")
    private Long id;

    @ApiModelProperty("概念id")
    private Long selfId;

    @ApiModelProperty("父概念id")
    private Long conceptId;

    @ApiModelProperty("概念名称")
    private String name;

    @ApiModelProperty("当前概念实体数量")
    private Long entityCount;

    @ApiModelProperty("概念(包含子概念)实体总数量")
    private Long entityTotal;

    @ApiModelProperty("概念下属性定义数量")
    private Integer attrDefinitionCount;

    @ApiModelProperty("概念模式完整度")
    private Double schemaIntegrity;

    @ApiModelProperty("置信度质量")
    private Double reliability;

    @ApiModelProperty("更新时间")
    private Date updateAt;
}
