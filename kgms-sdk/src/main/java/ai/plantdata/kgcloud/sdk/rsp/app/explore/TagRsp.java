package ai.plantdata.kgcloud.sdk.rsp.app.explore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/11 14:10
 */
@Getter
@Setter
@ApiModel("标签视图")
public class TagRsp {
    @ApiModelProperty("标签名称")
    private String name;
    @ApiModelProperty("来源")
    private String source;
    @ApiModelProperty("创建时间")
    private String creationTime;
    @ApiModelProperty("等级")
    private Integer grade;

}
