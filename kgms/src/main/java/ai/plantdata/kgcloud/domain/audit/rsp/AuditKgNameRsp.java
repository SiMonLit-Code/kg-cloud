package ai.plantdata.kgcloud.domain.audit.rsp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-12 10:55
 **/
@Data
public class AuditKgNameRsp {

    @ApiModelProperty("图谱唯一标识 kgName")
    private String kgName;

    @ApiModelProperty("图谱标题")
    private String title;
}
