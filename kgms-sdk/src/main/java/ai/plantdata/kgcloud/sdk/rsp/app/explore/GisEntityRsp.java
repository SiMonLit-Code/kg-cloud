package ai.plantdata.kgcloud.sdk.rsp.app.explore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/11 17:50
 */
@Getter
@Setter
@ApiModel("gis实体")
public class GisEntityRsp extends GraphEntityRsp {
    @ApiModelProperty("gis信息")
    private GisInfoRsp gis;
    @ApiModelProperty("开始时间")
    private Date startTime;
    @ApiModelProperty("结束时间")
    private Date endTime;
}
