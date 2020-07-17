package ai.plantdata.kgcloud.sdk.rsp;

import com.fasterxml.jackson.databind.node.ArrayNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Data
@ApiModel("图谱焦点模型")
public class GraphConfFocusRsp {
    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "entities")
    private ArrayNode entities;

    @ApiModelProperty(value = "焦点配置")
    private String focusConfig;

    @ApiModelProperty(value = "创建时间")
    private Date createAt;

    @ApiModelProperty(value = "更新时间")
    private Date updateAt;

}
