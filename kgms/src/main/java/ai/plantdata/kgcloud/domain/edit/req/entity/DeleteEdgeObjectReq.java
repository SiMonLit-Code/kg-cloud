package ai.plantdata.kgcloud.domain.edit.req.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: LinHo
 * @Date: 2019/11/25 15:53
 * @Description:
 */
@Data
@ApiModel("边对象属性删除模型")
public class DeleteEdgeObjectReq {

    @NotNull
    @ApiModelProperty(value = "关系id")
    private String objId;

    @NotNull
    @ApiModelProperty(value = "关系属性id")
    private Integer attrId;

    @NotNull
    @ApiModelProperty(value = "边id")
    private Integer seqNo;

    @NotNull
    @ApiModelProperty(value = "值域id")
    private Long entityId;
}
