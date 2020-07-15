package ai.plantdata.kgcloud.sdk.req;

import ai.plantdata.cloud.bean.BaseReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-05-20 16:58
 **/
@Data
public class ReasoningQueryReq extends BaseReq {

    @NotNull
    @NotBlank
    @ApiModelProperty("图谱名")
    private String kgName;

    @ApiModelProperty("推理名称 支持模糊搜索")
    private String name;

}
