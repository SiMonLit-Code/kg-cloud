package com.plantdata.kgcloud.plantdata.rsp.explore.gis;

import com.plantdata.kgcloud.plantdata.req.common.RelationBean;
import com.plantdata.kgcloud.plantdata.req.entity.EntityBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/9/23 18:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GisLocusOldRsp {
    private List<GisLocusRelationRsp> relationList;
    private List<EntityBean> entityList;
    private Integer level1HasNextPage;

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GisLocusRelationRsp extends RelationBean {
        public Long ruleId;
    }

}
