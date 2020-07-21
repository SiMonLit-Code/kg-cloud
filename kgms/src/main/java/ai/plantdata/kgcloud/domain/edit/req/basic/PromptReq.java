package ai.plantdata.kgcloud.domain.edit.req.basic;

import ai.plantdata.cloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/29 16:17
 * @Description:
 */
@Data
@ApiModel("概念实体同义提示查询模型")
public class PromptReq extends BaseReq {

    @NotEmpty
    @ApiModelProperty(value = "关键词")
    @Length(max = 50, message = "长度不能超过50")
    private String kw;

    @ApiModelProperty(value = "是否忽略大小写,默认忽略大小写")
    private Boolean ignore = true;

    @ApiModelProperty(value = "是否模糊查询,默认前缀搜索")
    private Boolean like = false;

    @ApiModelProperty(value = "是否查询概念")
    private Boolean concept = true;

    @ApiModelProperty(value = "是否查询实体")
    private Boolean entity = true;

    @ApiModelProperty(value = "是否查询属性")
    private Boolean attribute = false;

    @ApiModelProperty(value = "概念ids")
    private List<Long> conceptIds;

    @ApiModelProperty(value = "是否继承")
    private Boolean inherit = false;

    @ApiModelProperty(value = "消歧标识")
    @Length(max = 100, message = "长度不能超过100")
    private String meaningTag;

}
