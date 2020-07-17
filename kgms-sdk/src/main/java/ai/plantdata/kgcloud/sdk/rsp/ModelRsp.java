package ai.plantdata.kgcloud.sdk.rsp;

import ai.plantdata.kgcloud.sdk.req.ModelPrf;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@ApiModel("模型响应")
@Data
public class ModelRsp {

    @ApiModelProperty(value = "模型id")
    private Long id;

    @ApiModelProperty(value = "模型用户id")
    private String userId;

    @ApiModelProperty(value = "模型名称")
    private String modelName;

    @ApiModelProperty(value = "模型url")
    private String modelUrl;

    @ApiModelProperty(value = "模型类型")
    private Integer modelType;

    @ApiModelProperty(value = "模型prf值")
    private ModelPrf prf;

    @ApiModelProperty(value = "模型标签")
    private List<String> labels;

    @ApiModelProperty(value = "模型描述")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createAt;

    @ApiModelProperty(value = "修改时间")
    private Date updateAt;
}
