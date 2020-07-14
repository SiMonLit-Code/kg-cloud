package ai.plantdata.kgcloud.domain.graph.attr.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: LinHo
 * @Date: 2019/12/3 13:59
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("属性分组查询模型")
public class AttrGroupSearchReq {

    @ApiModelProperty(value = "属性分组id")
    private Long id;

    @ApiModelProperty(value = "是否读取属性定义详情")
    private Boolean readDetail = false;
}
