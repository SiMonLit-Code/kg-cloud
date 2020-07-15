package ai.plantdata.kgcloud.domain.edit.req.induce;

import ai.plantdata.kg.api.edit.validator.TypeRange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/28 18:53
 * @Description:
 */
@Data
@ApiModel("执行属性公有化模型")
public class InducePublicReq {

    @ApiModelProperty(value = "规约出的属性id")
    private Integer attributeId;

    @ApiModelProperty(value = "规约出的属性名称")
    private String attributeName;

    @ApiModelProperty(value = "属性类型， 0 数值属性，1 对象属性")
    @TypeRange
    @NotNull
    private Integer type;

    @ApiModelProperty(value = "私有属性名称列表")
    @NotNull
    private List<String> privateAttributeNames;

    @ApiModelProperty(value = "定义域")
    private Long domainId;

    @ApiModelProperty(value = "对象属性的值域id列表")
    private List<Long> rangeIds;

    @ApiModelProperty(value = "数值属性数据类型")
    private Integer dataType;

    @ApiModelProperty(value = "数值属性数据单位")
    private String dataUnit;

}
