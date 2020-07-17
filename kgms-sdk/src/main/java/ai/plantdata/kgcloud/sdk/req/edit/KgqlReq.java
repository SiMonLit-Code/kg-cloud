package ai.plantdata.kgcloud.sdk.req.edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: LinHo
 * @Date: 2019/12/4 18:47
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("kgql执行模型")
public class KgqlReq {
    @ApiModelProperty("查询语句")
    private String query;
}
