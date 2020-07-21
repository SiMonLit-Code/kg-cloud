package ai.plantdata.kgcloud.domain.edit.req.attr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @Author: LinHo
 * @Date: 2019/11/27 14:14
 * @Description:
 */
@Data
@ApiModel("根据meta删除关系模型")
public class RelationMetaReq {

    @ApiModelProperty(value = "来源")
    @Length(max = 50, message = "长度不能超过50")
    private String source;

    @ApiModelProperty(value = "批次号")
    private String batchNo;
}
