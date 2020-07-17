package ai.plantdata.kgcloud.domain.audit.rsp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * describe about this class
 *
 * @author DingHao
 * @date 2019/2/28 13:19
 */
@Data
public class ApiAuditRsp {
    @ApiModelProperty("标签")
    private String name;
    @ApiModelProperty("数量")
    private Long value;
}
