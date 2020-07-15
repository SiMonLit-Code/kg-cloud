package ai.plantdata.kgcloud.sdk.req;

import ai.plantdata.cloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * Created by plantdata-1007 on 2019/11/29.
 */
@ApiModel("图谱算法设置")
@Data
public class GraphConfAlgorithmReqList extends BaseReq {

    @ApiModelProperty("类别 1面板类 2统计类")
    private Integer type;

}
