package com.plantdata.kgcloud.domain.app.rsp;

import com.plantdata.kgcloud.sdk.rsp.app.explore.GisEntityRsp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/9/23 18:33
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GisLocusRsp {
    private List<GisLocusRelationVO> relationList;
    private List<GisEntityRsp> entityList;
    private Integer level1HasNextPage;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class GisLocusRelationVO {
        private String id;
        private Long fromId;
        private Integer attId;
        private Long toId;
        private Date startTime;
        private Date endTime;
        public Integer ruleId;
    }

}
