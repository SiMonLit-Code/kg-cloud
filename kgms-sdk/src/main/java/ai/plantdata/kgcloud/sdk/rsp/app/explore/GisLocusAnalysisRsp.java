package ai.plantdata.kgcloud.sdk.rsp.app.explore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/10/31 16:50
 */
@ToString
@Getter
@Setter
@ApiModel("gis轨迹分析视图")
public class GisLocusAnalysisRsp {
    @ApiModelProperty("是否有下一页 1 有 0无")
    private Integer hasNextPage;
    @ApiModelProperty("实例或者概念")
    private List<GisEntityRsp> entityList;
    @ApiModelProperty("关系")
    private List<GisRelationRsp> relationList;
}
