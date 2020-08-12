package ai.plantdata.kgcloud.sdk.req.app.explore.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/13 17:17
 */
@Getter
@Setter
@ApiModel("路径分析-参数(通用/必填)")
public class CommonPathReq {
    @NotNull
    @ApiModelProperty(value = "起点", required = true)
    private Long start;
    @NotNull
    @ApiModelProperty(value = "终点", required = true)
    private Long end;
    @ApiModelProperty("是否是最短路径")
    private boolean shortest;
    @ApiModelProperty("？")
    @Max(100000)
    private Integer degreeLimit;

}
