package ai.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-10 11:21
 **/
@ApiModel("模型调用")
@Data
public class KgmsCallReq {

    @ApiModelProperty(value = "待校验数据", required = true)
    @NotNull
    @Size(min = 1)
    private List<String> input;
}
