package com.plantdata.kgcloud.plantdata.req.explore;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 图探索参数的父类
 */
@Data
@NoArgsConstructor
public abstract class AbstrackGraphParameter {
    @ApiParam(value = "图谱名称", required = true)
    private String kgName;
    @Max(value = 30, message = "层数最多可查询30层")
    @Min(value = 1, message = "层数最小为1")
    private Integer distance = 1;
    private List<Long> replaceClassIds;
    private List<String> replaceClassIdsKey;
    private Boolean isRelationMerge = false;
    private Boolean privateAttRead = true;
    private List<Integer> allowAtts;
    private List<String> allowAttsKey;
    private List<Long> allowTypes;
    private List<String> allowTypesKey;
    private List<Integer> allowAttrGroups;
    private List<EntityScreeningBean> entityQuery;
    private GraphBean graphBean;
    private List<AttrScreeningBean> attAttFilters;
    private List<AttrScreeningBean> reservedAttFilters;
    private Boolean isInherit = false;

    public abstract List<Long> getIdList();

}
