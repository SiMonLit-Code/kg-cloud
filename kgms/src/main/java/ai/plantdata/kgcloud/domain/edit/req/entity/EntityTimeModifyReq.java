package ai.plantdata.kgcloud.domain.edit.req.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/11/20 14:09
 * @Description:
 */
@Data
@ApiModel("实体开始,截止时间修改模型")
public class EntityTimeModifyReq {

    @ApiModelProperty(value = "实体开始时间")
    private String fromTime;

    @ApiModelProperty(value = "实体截止时间")
    private String toTime;
}
