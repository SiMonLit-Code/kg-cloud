package ai.plantdata.kgcloud.domain.common.rsp.matedata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/4 10:39
 */
@Getter
@Setter
@ToString
@ApiModel("标签")
public class TagRsp {
    @ApiModelProperty("标签名称")
    private String name;
    @ApiModelProperty("理由")
    private String source;
    @ApiModelProperty("创建时间")
    private String creationTime;
    @ApiModelProperty("等级")
    private Integer grade;
}
