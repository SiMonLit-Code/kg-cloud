package ai.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-10 11:19
 **/
@ApiModel("模型新增/修改")
@Data
public class KgmsModelReq {


    @ApiModelProperty(value = "模型名称", required = true)
    @NotBlank
    private String modelName;

    @ApiModelProperty(value = "模型地址", required = true)
    @NotBlank
    private String modelUrl;

    @ApiModelProperty(value = "模型类型", required = true)
    @NotNull
    private Integer modelType;

    @ApiModelProperty(value = "模型prf值")
    private ModelPrf prf;

    @ApiModelProperty(value = "模型标签")
    @NotNull
    @Size(min = 1)
    private List<String> labels;

    @ApiModelProperty(value = "模型描述")
    private String remark;

}
