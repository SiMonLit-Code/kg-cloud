package com.plantdata.kgcloud.sdk.req.app;

import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/4 14:32
 */
@ApiModel("知识推荐参数")
@Getter
@Setter
public class KnowledgeRecommendReq extends BaseReq {

    @ApiModelProperty("实例id")
    @NotNull
    private Long entityId;
    @ApiModelProperty("关系方向。默认正向，0表示双向，1表示出发，2表示到达,默认0")
    private Integer direction = 1;
    @ApiModelProperty("推荐范围，格式为json数组的属性定义id 例:[1,2],必须指定范围")
    @NotNull
    private List<Integer> allowAttrs;
    @ApiModelProperty("推荐范围allowAttrs为空时生效，格式为json数组的属性定义唯一标识 例:[\"key1\",\"key2\"],必须指定范围")
    private List<String> allowAttrsKey;

}
