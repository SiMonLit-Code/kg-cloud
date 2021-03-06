package ai.plantdata.kgcloud.domain.edit.req.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author: LinHo
 * @Date: 2019/11/18 13:47
 * @Description:
 */
@Data
@ApiModel("摘要更新模型")
public class AbstractModifyReq {

    @ApiModelProperty(required = true)
    @NotNull
    @Min(value = 1,message = "不能修改图谱")
    private Long id;

    @ApiModelProperty(value = "摘要")
    @Length(max = 5000,message = "摘要描述不能超过5000")
    private String abs;
}
