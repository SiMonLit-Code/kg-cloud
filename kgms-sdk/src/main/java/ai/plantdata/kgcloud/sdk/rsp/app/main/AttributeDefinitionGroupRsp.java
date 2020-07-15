package ai.plantdata.kgcloud.sdk.rsp.app.main;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/4 14:13
 */
@ApiModel("属性分组")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttributeDefinitionGroupRsp {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("属性定义id")
    private List<Integer> attrDefIds;
}
