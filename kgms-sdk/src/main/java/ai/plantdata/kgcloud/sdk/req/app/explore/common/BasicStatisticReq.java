package ai.plantdata.kgcloud.sdk.req.app.explore.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/13 16:06
 */
@Getter
@Setter
@ApiModel("图分析统计-参数(通用)")
public class BasicStatisticReq {
    @ApiModelProperty(value = "key 为唯一标识，用于区分多组结果", required = true)
    private String key;
    @ApiModelProperty(value = "概念id", required = true)
    private Long conceptId;
    @ApiModelProperty(value = "属性id", required = true)
    private List<Integer> attrIdList;
}
