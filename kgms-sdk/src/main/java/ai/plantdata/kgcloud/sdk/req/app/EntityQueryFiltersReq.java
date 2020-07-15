package ai.plantdata.kgcloud.sdk.req.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/13 10:36
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EntityQueryFiltersReq extends CompareFilterReq {
    @ApiModelProperty("属性定义id")
    private Integer attrDefId;
    private Integer relation;
}
