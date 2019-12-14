package com.plantdata.kgcloud.plantdata.req.explore;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.plantdata.req.common.AttrSortBean;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonFiltersReq;
import com.plantdata.kgcloud.sdk.req.app.function.GraphCommonReqInterface;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 普通图探索类
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class GeneralGraphParameter extends AbstrackGraphParameter {

    private Long id;


    private String kw;
    private Integer direction = 0;
    private Integer highLevelSize = 10;

    private GraphRuleBean graphRule;


    private String graphRuleKgql;
    private Integer hyponymyDistance;

    private List<AttrSortBean> attSorts;
    private Integer pageNo = 1;
    private Integer pageSize = 10;


    @Override
    public List<Long> getIdList() {
        return Lists.newArrayList(getId());
    }

}
