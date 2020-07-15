package ai.plantdata.kgcloud.sdk.rsp.app.semantic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author ?
 */
@Getter
@Setter
@ApiModel("推理规则返回值")
public class GraphReasoningResultRsp {
    @ApiModelProperty("结果数量")
    private int count;
    @ApiModelProperty("结果三元组")
    private List<TripleBean> tripleList;
}
