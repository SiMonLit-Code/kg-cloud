package ai.plantdata.kgcloud.sdk.rsp.app.semantic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ?
 */
@Getter
@Setter
@ApiModel("推理参数详情")
public class TripleBean {
    @ApiModelProperty("起点")
    private NodeBean start;
    @ApiModelProperty("关系")
    private EdgeBean edge;
    @ApiModelProperty("终点")
    private NodeBean end;
    @ApiModelProperty("关系生成方式")
    private String reason;

}
