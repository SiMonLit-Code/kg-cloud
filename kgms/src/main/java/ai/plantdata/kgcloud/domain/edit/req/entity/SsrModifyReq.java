package ai.plantdata.kgcloud.domain.edit.req.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;

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
    @Digits(integer = 1,fraction = 2,message = "只保留两位小数")
    private Double score;

    @ApiModelProperty(value = "实体来源")
    @Length(max = 50, message = "长度不能超过50")
    private String source;

    @ApiModelProperty(value = "实体置信度")
    @DecimalMax(value = "1.0")
    @DecimalMin(value = "0.0")
    @Digits(integer = 1,fraction = 2,message = "只保留两位小数")
    private Double reliability;
}
