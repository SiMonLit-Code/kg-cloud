package ai.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * Created by plantdata-1007 on 2019/11/29.
 */
@ApiModel("图谱算法设置")
@Data
public class GraphConfAlgorithmReq {

    @ApiModelProperty("算法名称")
    private String algorithmName;

    @ApiModelProperty("接口")
    private String algorithmUrl;

    @ApiModelProperty("描述")
    private String remark;

    @ApiModelProperty("类别 1面板类 2统计类")
    private Integer type;


}
