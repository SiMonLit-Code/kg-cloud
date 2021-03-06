package ai.plantdata.kgcloud.sdk.req;

import ai.plantdata.cloud.bean.BaseReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-08 18:00
 **/
@Data
public class StandardSearchReq extends BaseReq {

    @ApiModelProperty(value = "搜索词")
    private String kw;

    @ApiModelProperty(value = "模式类型")
    private String modelType;
}
