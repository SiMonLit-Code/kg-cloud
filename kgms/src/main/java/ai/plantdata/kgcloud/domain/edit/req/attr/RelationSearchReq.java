package ai.plantdata.kgcloud.domain.edit.req.attr;

import ai.plantdata.cloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @Author: LinHo
 * @Date: 2019/11/27 09:58
 * @Description:
 */
@Data
@ApiModel("关系溯源搜索模型")
public class RelationSearchReq extends BaseReq {

    @ApiModelProperty(value = "概念id")
    private Long conceptId;

    @ApiModelProperty(value = "实体名称")
    @Length(max = 50, message = "长度不能超过50")
    private String entityName;

    @ApiModelProperty(value = "关系名称")
    @Length(max = 50, message = "长度不能超过50")
    private String name;

}
