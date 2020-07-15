package ai.plantdata.kgcloud.sdk.req.edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/11/26 11:02
 * @Description:
 */
@Data
@ApiModel("批量添加属性定义结果模型")
public class AttrDefinitionBatchRsp extends AttrDefinitionVO {

    @ApiModelProperty(value = "添加属性定义处理结果")
    private String note;

}
