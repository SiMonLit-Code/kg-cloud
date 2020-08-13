package ai.plantdata.kgcloud.sdk.req;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Bovin
 * @description
 * @since 2020-08-12 17:16
 **/
@ApiModel("图快照")
@Data
public class GraphConfSnapshotReq {

    @ApiModelProperty(value = "图谱名称")
    @NotBlank(message = "kgname 不能为空")
    private String kgName;

    @ApiModelProperty(value = "spaId")
    @NotBlank
    private String spaId;

    @Length(max = 50, message = "长度不能超过50")
    @ApiModelProperty(required = true,value = "快照名称")
    private String name;

    @Length(max = 255, message = "长度不能超过255")
    @ApiModelProperty(value = "快照描述")
    private String remark;

    @ApiModelProperty(required = true,value = "快照配置")
    private Object snapshotConfig;

}
