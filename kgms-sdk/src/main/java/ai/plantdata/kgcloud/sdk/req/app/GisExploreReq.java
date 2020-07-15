package ai.plantdata.kgcloud.sdk.req.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/4 11:14
 */
@Getter
@Setter
public class GisExploreReq {

    @ApiModelProperty("区域选取类型 $box方形 $centerSphere圆形")
    private String filterType;
    @ApiModelProperty("经度范围,数组格式，值为2个元素，box时例：[[-75,40],[-70,50]]，表示2个坐标点组成的矩形，center时例：[[-74,40],10]，第一个表示圆心坐标，第二个表示半径")
    private List gisFilters;
    @ApiModelProperty("开始时间 yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String fromTime;
    @ApiModelProperty("结束时间 yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String toTime;
    @ApiModelProperty("分页")
    private PageReq page;
}
