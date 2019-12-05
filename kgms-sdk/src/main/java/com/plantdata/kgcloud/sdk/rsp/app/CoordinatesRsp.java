package com.plantdata.kgcloud.sdk.rsp.app;


import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicEntityRsp;
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
public class CoordinatesRsp {

    @ApiModelProperty("实例列表")
    private List<CoordinatesEntityRsp> entityList;

    @Getter
    @Setter
    private static class CoordinatesEntityRsp extends BasicEntityRsp {
        private Long cluster;
        private Double distance;
    }
}
