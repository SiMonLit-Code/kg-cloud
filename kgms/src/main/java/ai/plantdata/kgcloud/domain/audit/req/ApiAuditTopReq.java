package ai.plantdata.kgcloud.domain.audit.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-12 11:45
 **/
@Data
public class ApiAuditTopReq extends ApiAuditReq {

    @ApiModelProperty("排序类型 1 调用次数 2 成功率 3 失败率 ， 默认 按  调用次数  排序")
    private Integer order;
}
