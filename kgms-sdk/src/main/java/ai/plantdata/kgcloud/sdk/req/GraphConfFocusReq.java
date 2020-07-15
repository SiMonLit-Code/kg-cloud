package ai.plantdata.kgcloud.sdk.req;

import com.fasterxml.jackson.databind.node.ArrayNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by plantdata-1007 on 2019/11/29.
 */
@ApiModel("焦点设置")
@Data
public class GraphConfFocusReq {


    @NotBlank
    @ApiModelProperty(value = "类型",required = true)
    private String type;

    @ApiModelProperty("entities")
    private ArrayNode entities;

}
