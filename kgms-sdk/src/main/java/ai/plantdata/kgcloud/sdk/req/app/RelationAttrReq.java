package ai.plantdata.kgcloud.sdk.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/12 16:55
 */
@Getter
@Setter
@ApiModel("边属性过滤")
public class RelationAttrReq extends CompareFilterReq {
    @NonNull
    @ApiModelProperty(value = "属性定义id",required = true)
    private Integer attrId;
    @ApiModelProperty(value = "边属性序号",required = true)
    @NonNull
    private Integer seqNo;
}
