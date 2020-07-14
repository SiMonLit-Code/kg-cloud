package ai.plantdata.kgcloud.domain.edit.req.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/11/18 11:57
 * @Description:
 */
@Data
@ApiModel("业务信息更新模型")
public class AdditionalModifyReq {

    @ApiModelProperty(required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(value = "业务配置")
    private Map additional;
}
