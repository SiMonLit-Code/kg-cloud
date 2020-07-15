package ai.plantdata.kgcloud.sdk.req.app.sematic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/3 14:41
 */
@Getter
@Setter
public class ReasoningReq {

    @ApiModelProperty(value = "推理规则语言")
    private String ruleConfig;
    @ApiModelProperty(value = "初始节点")
    private List<Long> ids;
    @ApiModelProperty("默认0")
    private int pos;
    @ApiModelProperty("默认5")
    private int size;
}
