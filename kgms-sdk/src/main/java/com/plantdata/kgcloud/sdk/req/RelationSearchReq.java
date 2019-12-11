package com.plantdata.kgcloud.sdk.req;

import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.sdk.req.app.RelationAttrReq;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/11 16:44
 */
@Getter
@Setter
public class RelationSearchReq extends BaseReq {

    private List<Long> entityIds;
    private List<Integer> attrIds;
    private List<String> attrKeys;
    private List<Long> attrValueIds;
    private String attrTimeFrom;
    private String attrTimeTo;
    private Integer direction = 0;
    private List<RelationAttrReq> edgeAttrQuery;
}
