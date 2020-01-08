package com.plantdata.kgcloud.sdk.req.app.explore.common;


import com.plantdata.kgcloud.sdk.req.app.EntityQueryFiltersReq;
import com.plantdata.kgcloud.sdk.req.app.RelationAttrReq;
import com.plantdata.kgcloud.sdk.req.app.dataset.PageReq;
import com.plantdata.kgcloud.sdk.req.app.function.AttrDefKeyReqInterface;
import com.plantdata.kgcloud.sdk.req.app.function.ConceptKeyListReqInterface;
import com.plantdata.kgcloud.sdk.req.app.function.GraphReqAfterInterface;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * @author cjw
 * 时序和普通 图探索参数的父类
 */
@Getter
@Setter
@ToString
public class BasicGraphExploreReq implements AttrDefKeyReqInterface, ConceptKeyListReqInterface, GraphReqAfterInterface {


    @ApiModelProperty("要替换的概念id")
    private List<Long> replaceClassIds;
    @ApiModelProperty("实例id")
    private List<String> replaceClassKeys;
    @ApiModelProperty("读取层数,最小1最大10")
    @Min(1)
    @Max(10)
    private Integer distance = 1;
    @ApiModelProperty("是否关系合并")
    private boolean relationMerge;
    @ApiModelProperty(value = "allowTypes字段指定的概念是否继承", required = true)
    private boolean isInherit;
    @ApiModelProperty("在指定属性范围内查询")
    private List<Integer> allowAttrs;
    @ApiModelProperty("在指定属性范围内查询")
    private List<String> allowAttrsKey;
    @ApiModelProperty("查询指定的概念，格式为json数组，默认为查询全部")
    private List<Long> allowConcepts;
    @ApiModelProperty("排除指定的概念，格式为json数组，默认为查询全部")
    private List<Long> disAllowConcepts;
    @ApiModelProperty("查询指定的概念的唯一标识，格式为json数组，默认为查询全部")
    private List<String> allowConceptsKey;
    @ApiModelProperty("指定屬性分组")
    private List<Long> allowAttrGroups;
    @ApiModelProperty("分页")
    private PageReq page;

    @ApiModelProperty("实例筛选")
    private List<EntityQueryFiltersReq> entityFilters;
    @ApiModelProperty("边属性过滤条件")
    private List<RelationAttrReq> edgeAttrFilters;
    @ApiModelProperty("要保留的边属性过滤条件")
    private List<RelationAttrReq> reservedEdgeAttrFilters;
    @ApiModelProperty("要求的参数")
    private BasicGraphExploreRsp graphReq;

}
