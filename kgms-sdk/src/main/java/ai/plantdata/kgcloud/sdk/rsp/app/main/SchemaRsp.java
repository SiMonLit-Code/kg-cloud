package ai.plantdata.kgcloud.sdk.rsp.app.main;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/1 15:27
 */
@Setter
@Getter
@ToString
@ApiModel("概念和属性定义视图")
public class SchemaRsp {

    @ApiModelProperty("概念")
    private List<BaseConceptRsp> types;
    @ApiModelProperty("属性定义")
    private List<AttributeDefinitionRsp> attrs;
    @ApiModelProperty("属性分组")
    private List<AttributeDefinitionGroupRsp> attrGroups;
    @ApiModelProperty("顶层概念")
    private String kgTitle;
}
