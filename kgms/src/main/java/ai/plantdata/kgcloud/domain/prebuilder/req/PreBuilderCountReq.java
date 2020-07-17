package ai.plantdata.kgcloud.domain.prebuilder.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "预构建模式属性引入状态统计")
public class PreBuilderCountReq {

    @ApiModelProperty("全部")
    private Integer all;

    @ApiModelProperty("可引入")
    private Integer introduction;

    @ApiModelProperty("已引入")
    private Integer haveIntroduction;

    @ApiModelProperty("冲突")
    private Integer conflict;
}
