package ai.plantdata.kgcloud.sdk.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Bovin
 * @description
 * @since 2020-08-12 17:16
 **/
@ApiModel("图快照")
@Getter
@Setter
public class GraphConfSnapshotRsp {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "图谱名称")
    private String kgName;

    @ApiModelProperty(value = "spaId")
    private String spaId;
}
