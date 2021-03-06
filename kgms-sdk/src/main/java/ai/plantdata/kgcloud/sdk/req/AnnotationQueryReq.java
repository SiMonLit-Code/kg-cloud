package ai.plantdata.kgcloud.sdk.req;

import ai.plantdata.cloud.bean.BaseReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-09 20:57
 **/
@Data
public class AnnotationQueryReq extends BaseReq {
    @ApiModelProperty("关键词")
    private String name;
    @ApiModelProperty("任务ID")
    private String taskId;
}
