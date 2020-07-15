package ai.plantdata.kgcloud.sdk.req.app.nlp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/6 18:32
 */
@Getter
@Setter
@ApiModel("实体链接-参数")
public class EntityLinkingReq {
    @ApiModelProperty(value = "概念id", required = true)
    @NotEmpty
    private List<Long> conceptIds;
    @ApiModelProperty(value = "要链接的文本", required = true)
    @NotBlank
    private String text;
}
