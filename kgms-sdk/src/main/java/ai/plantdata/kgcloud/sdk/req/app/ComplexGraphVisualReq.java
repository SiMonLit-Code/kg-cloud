package ai.plantdata.kgcloud.sdk.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/23 10:07
 */
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ApiModel("复杂图算法可视化-参数")
public class ComplexGraphVisualReq {
    @ApiModelProperty("执行返回的azkId")
    @NotNull
    private Long azkId;
    @ApiModelProperty("算法类型")
    @NotBlank
    private String type;
    private Integer size = 100;
}
