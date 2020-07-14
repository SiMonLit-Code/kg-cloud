package ai.plantdata.kgcloud.sdk.rsp;

import com.fasterxml.jackson.databind.node.ArrayNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Data
@ApiModel("图谱问答模型")
public class GraphConfQaRsp {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "kgName")
    private String kgName;

    @ApiModelProperty(value = "类型")
    private Integer type;

    @ApiModelProperty(value = "问题")
    private String question;

    @ApiModelProperty(value = "count")
    private int count;

    @ApiModelProperty(value = "选择概念")
    private List<Long> conceptIds;

    @ApiModelProperty(value = "优先")
    private Integer priority;

    @ApiModelProperty(value = "创建时间")
    private Date createAt;

    @ApiModelProperty(value = "更新时间")
    private Date updateAt;
}
