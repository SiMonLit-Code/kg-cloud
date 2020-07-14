package ai.plantdata.kgcloud.domain.edit.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: LinHo
 * @Date: 2019/12/4 11:57
 * @Description:
 */
@Data
@ApiModel("词典查询结果模型")
public class DictRsp {

    @ApiModelProperty(value = "领域词id")
    private String _id;

    @ApiModelProperty(value = "领域词")
    private String name;

    @ApiModelProperty(value = "词性")
    private String nature;

    @ApiModelProperty(value = "概念id")
    private Long conceptId;

    @ApiModelProperty(value = "概念名称")
    private String conceptName;

    @ApiModelProperty(value = "实体id")
    private Long entityId;

    @ApiModelProperty(value = "实体名称")
    private String entityName;

    @ApiModelProperty(value = "词频")
    private Double frequency;

    @ApiModelProperty(value = "更新时间")
    private Date lastModifyTime;
}
