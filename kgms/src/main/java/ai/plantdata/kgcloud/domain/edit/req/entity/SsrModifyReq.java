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
@ApiModel("权重,来源,置信度修改模型")
public class SsrModifyReq {

    @ApiModelProperty(value = "实体权重")
    @DecimalMax(value = "1.0")
    @DecimalMin(value = "0.0")
    private Double score;

    @ApiModelProperty(value = "实体来源")
    private String source;

    @ApiModelProperty(value = "实体置信度")
    @DecimalMax(value = "1.0")
    @DecimalMin(value = "0.0")
    private Double reliability;
}
