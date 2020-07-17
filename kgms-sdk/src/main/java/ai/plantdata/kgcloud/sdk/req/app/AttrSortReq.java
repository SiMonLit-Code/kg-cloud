package ai.plantdata.kgcloud.sdk.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author cjw
 * @date 2019-11-01 10:30:43
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ApiModel("属性排序")
public class AttrSortReq {
    @ApiModelProperty("数值属性id")
    private Integer attrId;
    @ApiModelProperty("边数值属性id")
    private Integer seqNo;
    @ApiModelProperty("排序方式 -1或1")
    private Integer sort;

}
