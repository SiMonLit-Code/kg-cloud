package ai.plantdata.kgcloud.sdk.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 15:28
 */
@Getter
@Setter
@ApiModel("sqarQl查询参数")
public class SparQlReq {
    @ApiModelProperty("查询语句")
    private String query;
    @ApiModelProperty("数量 默认0")
    private int size;
}
