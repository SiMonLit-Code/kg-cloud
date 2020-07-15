package ai.plantdata.kgcloud.domain.edit.req.attr;

import ai.plantdata.kg.api.edit.validator.TypeRange;
import ai.plantdata.kgcloud.domain.edit.vo.IdNameVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/26 17:14
 * @Description:
 */
@Data
@ApiModel("属性定义模板创建模型")
public class AttrTemplateReq {

    @NotEmpty
    @NotBlank
    @ApiModelProperty(value = "属性名称")
    private String name;

    @ApiModelProperty(value = "属性名称别名")
    private String alias;

    @ApiModelProperty(value = "0：数值，1：对象", allowableValues = "0,1")
    @NotNull
    @TypeRange
    private Integer type;

    @NotNull
    @ApiModelProperty(value = "属性定义域")
    private Long domainValue;

    @ApiModelProperty(value = "属性值域")
    private List<IdNameVO> range;

    @ApiModelProperty(value = "属性类型")
    private Integer dataType;

    @ApiModelProperty(value = "属性单位")
    private String dataUnit;

    @ApiModelProperty(value = "对象属性是否唯一,0:N,1:Y")
    private Integer functional = 0;

    @ApiModelProperty(value = "对象属性方向,0:正向,1:无向")
    private Integer direction = 0;

}
