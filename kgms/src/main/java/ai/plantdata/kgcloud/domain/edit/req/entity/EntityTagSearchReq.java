package ai.plantdata.kgcloud.domain.edit.req.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * @Author: LinHo
 * @Date: 2019/12/12 19:22
 * @Description:
 */
@Getter
@Setter
@ApiModel("实体标签提示模型")
public class EntityTagSearchReq {
    @ApiModelProperty(value = "概念id")
    private Long conceptId;

    @ApiModelProperty(value = "标签关键字前缀搜索")
    @Length(max = 50, message = "长度不能超过50")
    private String kw;

    @ApiModelProperty(value = "数量")
    private Integer limit = 10;
}
