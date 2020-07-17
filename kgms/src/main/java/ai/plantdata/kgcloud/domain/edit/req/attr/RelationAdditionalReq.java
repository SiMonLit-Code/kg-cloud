package ai.plantdata.kgcloud.domain.edit.req.attr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/11/27 16:15
 * @Description:
 */
@Data
@ApiModel("关系业务配置模型")
public class RelationAdditionalReq {
    @NotEmpty
    @ApiModelProperty(value = "关系id")
    private String tripleId;

    @ApiModelProperty(value = "业务配置信息")
    private Map<String, Object> additional;

    @JsonIgnore
    private Map<String, Object> metaData = new HashMap<>(1);
}
