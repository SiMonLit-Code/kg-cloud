package ai.plantdata.kgcloud.sdk.req.wrapper;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/19 14:49
 */
@Getter
@Setter
@ApiModel(" Wrapper预览")
public class WrapperPreviewReq {
    @NonNull
    @ApiModelProperty("配置")
    private List<FieldConfigBean> config;
    @NonNull
    @ApiModelProperty("html")
    private String html;
}
