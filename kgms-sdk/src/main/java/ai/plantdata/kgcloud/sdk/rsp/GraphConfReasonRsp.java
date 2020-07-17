package ai.plantdata.kgcloud.sdk.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Data
@ApiModel("图谱推理模型")
public class GraphConfReasonRsp {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "kgName")
    private String kgName;

    @ApiModelProperty(value = "关系名称")
    private String ruleName;

    @ApiModelProperty(value = "关系配置")
    private Map<String,Object> ruleConfig;

    @ApiModelProperty(value = "创建时间")
    private Date createAt;

    @ApiModelProperty(value = "更新时间")
    private Date updateAt;
}
