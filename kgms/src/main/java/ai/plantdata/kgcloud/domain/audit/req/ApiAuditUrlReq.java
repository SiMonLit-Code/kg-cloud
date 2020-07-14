package ai.plantdata.kgcloud.domain.audit.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-12 11:46
 **/
@Data
public class ApiAuditUrlReq extends ApiAuditReq {


    @ApiModelProperty("分组类型 1 按月  2 按天 3 按小时 ， 默认 按天  分组")
    private Integer group;
}
