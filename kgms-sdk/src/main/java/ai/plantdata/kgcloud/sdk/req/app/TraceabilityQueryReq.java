package ai.plantdata.kgcloud.sdk.req.app;

import ai.plantdata.cloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-07 17:55
 **/
@Getter
@Setter
@ApiModel("溯源信息查实体")
public class TraceabilityQueryReq extends BaseReq {

    @ApiModelProperty("数仓标识")
    private String dataName;

    @ApiModelProperty("表标识")
    private String tableName;

    @ApiModelProperty("数据id")
    private String id;
}
