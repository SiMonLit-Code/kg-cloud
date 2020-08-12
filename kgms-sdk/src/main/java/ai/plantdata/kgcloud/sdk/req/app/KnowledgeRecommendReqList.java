package ai.plantdata.kgcloud.sdk.req.app;

import ai.plantdata.kgcloud.sdk.req.app.function.AttrDefListKeyReqInterface;
import ai.plantdata.kgcloud.sdk.req.app.function.EntityName2Id;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/4 14:32
 */
@ApiModel("知识推荐参数")
@Data
public class KnowledgeRecommendReqList extends PageReq implements AttrDefListKeyReqInterface, EntityName2Id {

    @ApiModelProperty("实例id")
    private Long entityId;
    @ApiModelProperty("实例名称")
    private String kw;
    @ApiModelProperty("关系方向。默认正向，0表示双向，1表示出发，2表示到达,默认0")
    private Integer direction = 1;
    @ApiModelProperty("推荐范围，格式为json数组的属性定义id 例:[1,2],allowAttrs,allowAttrsKey不能同时为空")
    private List<Integer> allowAttrs;
    @ApiModelProperty(value = "推荐范围allowAttrs为空时生效，格式为json数组的属性定义唯一标识 例:[\"key1\",\"key2\"]")
    private List<String> allowAttrsKey;

}
