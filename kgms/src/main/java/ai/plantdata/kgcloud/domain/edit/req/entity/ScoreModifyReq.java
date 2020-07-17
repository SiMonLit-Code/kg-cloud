package ai.plantdata.kgcloud.domain.edit.req.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

/**
 * @Author: LinHo
 * @Date: 2019/11/20 14:04
 * @Description:
 */
@Data
@ApiModel("权重修改模型")
public class ScoreModifyReq {

    @ApiModelProperty(value = "实体权重")
    @DecimalMax(value = "1.0")
    @DecimalMin(value = "0.0")
    private Double score;

}
