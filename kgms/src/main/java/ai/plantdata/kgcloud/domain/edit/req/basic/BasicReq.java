package ai.plantdata.kgcloud.domain.edit.req.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author: LinHo
 * @Date: 2019/12/11 15:03
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("概念或实体详情模型")
public class BasicReq {

    @ApiModelProperty(required = true, value = "概念或实体id")
    @NotNull
    private Long id;

    @ApiModelProperty(required = true, value = "true-实体,false-概念")
    @NotNull
    private Boolean isEntity;
}
