package com.plantdata.kgcloud.sdk.req.app.explore.common;

import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.sdk.req.app.EntityQueryFiltersReq;
import com.plantdata.kgcloud.sdk.req.app.RelationAttrReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author cjw
 * 时序和普通 图探索参数的父类
 */
@Getter
@Setter
@ToString
public class BasicGraphExploreReq {


    @ApiModelProperty("查询边关系的方向，0表示双向，1表示出发，2表示到达,默认0")
    @Pattern(regexp = "^[0-2]$")
    private Integer direction = 0;
    @ApiModelProperty("第二层以上节点查询个数，如果指定，第2层及第2层以上返回的节点以此数为限")
    @Min(value = 0, message = "highLevelSize最小为0")
    private Integer highLevelSize = 10;
    @ApiModelProperty("要替换的概念id")

    private List<Long> replaceConceptIds;
    @ApiModelProperty("实例id")
    private List<String> replaceConceptKeys;
    @ApiModelProperty("实例筛选")
    private List<EntityQueryFiltersReq> entityQueryFilters;
    @ApiModelProperty("读取层数")
    private Integer distance;
    @ApiModelProperty("是否关系合并")
    private boolean relationMerge;
    @ApiModelProperty("allowTypes字段指定的概念是否继承")
    private boolean isInherit;
    private List<Integer> allowAttrs;
    private List<String> allowAttrsKey;
    @ApiModelProperty("查询指定的概念，格式为json数组，默认为查询全部")
    private List<Long> allowConcepts;
    @ApiModelProperty("排除指定的概念，格式为json数组，默认为查询全部")
    private List<Long> disAllowConcepts;
    @ApiModelProperty("查询指定的概念的唯一标识，格式为json数组，默认为查询全部")
    private List<String> allowConceptsKey;
    @ApiModelProperty("指定屬性分组")
    private List<Long> allowAttrGroups;
    @ApiModelProperty("边属性过滤条件")
    private RelationAttrReq relationAttr;
    @ApiModelProperty("要保留的边属性过滤条件")
    private RelationAttrReq reservedAttFilters;
    @ApiModelProperty("分页")
    private BaseReq page;


}
