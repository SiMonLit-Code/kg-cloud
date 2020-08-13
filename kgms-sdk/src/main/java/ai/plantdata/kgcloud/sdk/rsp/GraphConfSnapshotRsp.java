package ai.plantdata.kgcloud.sdk.rsp;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * @author Bovin
 * @description
 * @since 2020-08-12 17:16
 **/
@ApiModel("图快照")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraphConfSnapshotRsp {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "图谱名称")
    private String kgName;

    @ApiModelProperty(value = "spaId")
    private String spaId;

    @ApiModelProperty(value = "快照名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "配置")
    private Object snapshotConfig;
}
