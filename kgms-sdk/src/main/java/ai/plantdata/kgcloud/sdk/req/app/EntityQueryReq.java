package ai.plantdata.kgcloud.sdk.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 10:53
 */
@Getter
@Setter
@ApiModel("实体查询参数")
public class EntityQueryReq extends PageReq {
    @ApiModelProperty("实体所属概念ID")
    private Long conceptId;
    @ApiModelProperty("conceptId为空时生效")
    private String conceptKey;
    @ApiModelProperty("筛选条件[{\"attrDefId\":\"1\",\"$eq\":\"abc\"}]")
    private List<DataAttrReq> dataAttrFilters;

}
