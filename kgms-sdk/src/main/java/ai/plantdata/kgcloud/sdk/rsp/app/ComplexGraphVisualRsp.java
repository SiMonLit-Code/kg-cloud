package ai.plantdata.kgcloud.sdk.rsp.app;


import ai.plantdata.kgcloud.sdk.rsp.app.explore.CoordinateReq;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.GraphEntityRsp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/14 15:52
 */
@ApiModel("复杂图可视化视图")
@Getter
@Setter
public class ComplexGraphVisualRsp {

    @ApiModelProperty("实例列表")
    private List<CoordinatesEntityRsp> entityList;

    @Getter
    @Setter
    public static class CoordinatesEntityRsp extends GraphEntityRsp {
        private Long cluster;
        @ApiModelProperty("层数")
        private Double distance;
        @ApiModelProperty("坐标信息")
        private CoordinateReq coordinates;
    }
}
