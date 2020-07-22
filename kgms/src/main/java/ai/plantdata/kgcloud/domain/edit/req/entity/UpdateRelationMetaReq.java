package ai.plantdata.kgcloud.domain.edit.req.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/11/25 11:21
 * @Description:
 */
@Data
@ApiModel("对象属性Metadata修改模型")
public class UpdateRelationMetaReq {

    @NotEmpty
    @ApiModelProperty(value = "关系id")
    private String tripleId;

    @ApiModelProperty(value = "关系权重")
    @DecimalMax(value = "1.0")
    @DecimalMin(value = "0.0")
    @Digits(integer = 1,fraction = 2,message = "只保留两位小数")
    private String score;

    @ApiModelProperty(value = "关系来源")
    private String source;

    @ApiModelProperty(value = "关系来源理由")
    private String sourceReason;

    @ApiModelProperty(value = "关系置信度")
    @DecimalMax(value = "1.0")
    @DecimalMin(value = "0.0")
    @Digits(integer = 1,fraction = 2,message = "只保留两位小数")
    private String reliability;

    @ApiModelProperty(value = "关系开始时间")
    private String attrTimeFrom;

    @ApiModelProperty(value = "关系截止时间")
    private String attrTimeTo;

    @JsonIgnore
    private Map<String, Object> metaData = new HashMap<>(4);
}
