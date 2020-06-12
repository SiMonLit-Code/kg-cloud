package com.plantdata.kgcloud.sdk.req.app;

import com.plantdata.kgcloud.sdk.validator.ChooseCheck;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-12 16:33
 **/
@Data
public class KnowledgeRecommendCommonFilterReq {

    @ApiModelProperty("是否读取私有属性，默认读取")
    private Boolean privateAttRead;
    @ApiModelProperty("上下位关系的读取层数，0表示不读取，默认为0")
    @Min(value = 0, message = "hyponymyDistance最小值为0")
    @Max(value = 100, message = "hyponymyDistance最大值为100")
    private Integer hyponymyDistance;
    @ApiModelProperty("第二层以上节点查询个数，如果指定，第2层及第2层以上返回的节点以此数为限")
    @Min(value = 1, message = "highLevelSize最小为0")
    private Integer highLevelSize = 10;
    @ApiModelProperty("查询边关系的方向，0表示双向，1表示正向，-1表示反向,默认0")
    @ChooseCheck(value = "[0,-1,1]")
    private int direction = 0;
    @ApiModelProperty(value = "边附加属性排序参数")
    private List<AttrSortReq> edgeAttrSorts;

    @ApiModelProperty("推荐范围，格式为json数组的属性定义id 例:[1,2],allowAttrs,allowAttrsKey不能同时为空")
    private List<Integer> allowAttrs;
    @ApiModelProperty(value = "推荐范围allowAttrs为空时生效，格式为json数组的属性定义唯一标识 例:[\"key1\",\"key2\"]")
    private List<String> allowAttrsKey;

}
