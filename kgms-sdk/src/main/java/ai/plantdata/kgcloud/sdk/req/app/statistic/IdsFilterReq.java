package ai.plantdata.kgcloud.sdk.req.app.statistic;

import ai.plantdata.kgcloud.sdk.validator.ListLengthCheck;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Administrator
 */
@Getter
@Setter
@ApiModel("id筛选-参数")
public class IdsFilterReq<T> {
    @ApiModelProperty("层级")

    private Integer layer;
    @ApiModelProperty(value = "ids",required = true)
    @NotNull
    @ListLengthCheck(min = 1)
    private List<T> ids;
}
