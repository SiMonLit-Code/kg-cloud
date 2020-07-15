package ai.plantdata.kgcloud.domain.graph.clash.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xiezhenxiang 2019/12/12
 */
@Data
@ApiModel
public class ClashToGraphReq {

    @ApiModelProperty(value = "冲突记录ID", required = true)
    private String id;

    @ApiModelProperty(value = "实体ID", required = true)
    private Long entityId;

    @ApiModelProperty(value = "属性ID", required = true)
    private Integer attrId;

    @ApiModelProperty(value = "属性类型", required = true)
    private Integer attrType;

    @ApiModelProperty(value = "属性值", required = true)
    private String attrValue;
}
