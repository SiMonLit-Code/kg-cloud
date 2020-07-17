package ai.plantdata.kgcloud.sdk.rsp.app.explore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/13 15:42
 */
@ApiModel("时序图探索参数")
public class TimingBasicGraphExploreRsp extends BasicGraphExploreRsp {
    @ApiModelProperty("实例")
    private List<GraphEntityRsp> entityList;
}
