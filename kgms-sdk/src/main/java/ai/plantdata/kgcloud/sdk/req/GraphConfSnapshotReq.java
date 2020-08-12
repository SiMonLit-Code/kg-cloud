package ai.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Bovin
 * @description
 * @since 2020-08-12 17:16
 **/
@ApiModel("图快照")
@Data
public class GraphConfSnapshotReq {

    @ApiModelProperty(value = "图谱名称")
    private String kgName;

    @ApiModelProperty(value = "spaId")
    private String spaId;
}
