package ai.plantdata.kgcloud.domain.audit.rsp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-16 21:00
 **/
@Data
public class ApiAuditVoRsp {

    @ApiModelProperty("路径")
    private String name;

    @ApiModelProperty("数量")
    private Long value;

    @ApiModelProperty("时间")
    private String time;
}
