package ai.plantdata.kgcloud.domain.edit.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/11/28 14:36
 * @Description:
 */
@Data
@ApiModel("实体融合候选集模型")
public class EntityMergeRsp {

    @ApiModelProperty(value = "主体id")
    private Long id;

    @ApiModelProperty(value = "客体id")
    private Long toMergeId;

    @ApiModelProperty(value = "融合分值")
    private Double score;

    @ApiModelProperty(value = "主体名称")
    private String name;

    @ApiModelProperty(value = "主体消岐标识")
    private String meaningTag;

    @ApiModelProperty(value = "主体概念id")
    private Long classId;

    @ApiModelProperty(value = "客体名称")
    private String toMergeName;

    @ApiModelProperty(value = "客体消岐标识")
    private String toMergeMeaningTag;

    @ApiModelProperty(value = "融合状态")
    private Integer status;
}
