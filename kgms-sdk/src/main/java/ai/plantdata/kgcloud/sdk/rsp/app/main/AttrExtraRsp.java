package ai.plantdata.kgcloud.sdk.rsp.app.main;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 15:02
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("边属性-视图")
public class AttrExtraRsp {
    @ApiModelProperty("边属性编号")
    private Integer seqNo;
    @ApiModelProperty("边属性名称")
    private String name;
    @ApiModelProperty("边数值属性数据类型")
    private Integer dataType;
    @ApiModelProperty("类型")
    private Integer type;
    private String dataUnit;
    private Integer indexed;
    private List<Long> objRange;
}
