package ai.plantdata.kgcloud.sdk.rsp.app.main;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/4 14:05
 */
@ApiModel("属性定义")
@Getter
@Setter
public class AttributeDefinitionRsp {

    @ApiModelProperty("属性定义id")
    private Integer id;
    @ApiModelProperty("属性定义key")
    private String key;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("属性定义类型")
    private Integer type;
    @ApiModelProperty("属性值域")
    private List<Long> rangeValue;
    @ApiModelProperty("所属概念id")
    private Long domainValue;
    @ApiModelProperty("属性值的类型")
    private Integer dataType;
    private List<AttrExtraRsp> extraInfos;
    @ApiModelProperty("方向")
    private Integer direction;
    @ApiModelProperty("属性值的附加信息")
    private Map<String, Object> additionalInfo;


}
