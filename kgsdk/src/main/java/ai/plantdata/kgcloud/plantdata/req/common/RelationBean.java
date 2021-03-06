package ai.plantdata.kgcloud.plantdata.req.common;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class RelationBean extends RelationMetaData {
    private static final String RELATION_START_TIME_SSE_KEY = "开始时间";
    private static final String RELATION_END_TIME_SSE_KEY = "结束时间";
    private String id;
    private Long from;
    private Long to;
    private Long attId;
    /**
     * 0 普通关系 1推理
     */
    private int type;
    private String attName;
    private List<String> startTime;
    private List<String> endTime;
    private List<RelationInfoBean> nRInfo;
    private List<RelationInfoBean> oRInfo;
    private Integer direction;


    public void addStartTime(String startTime) {
        if (this.startTime == null) {
            this.startTime = new ArrayList<>();
        }
        this.startTime.add(startTime);
    }


    public void addEndTime(String endTime) {
        if (this.endTime == null) {
            this.endTime = new ArrayList<>();
        }
        this.endTime.add(endTime);
    }


    public void addrNumInfo(RelationInfoBean one) {
        if (this.nRInfo == null) {
            this.nRInfo = new ArrayList<>();
        }
        this.nRInfo.add(one);
    }

    public List<RelationInfoBean> getnRInfo() {
        return nRInfo;
    }

    public void setnRInfo(List<RelationInfoBean> nRInfo) {
        this.nRInfo = nRInfo;
    }

    public List<RelationInfoBean> getoRInfo() {
        return oRInfo;
    }

    public void setoRInfo(List<RelationInfoBean> oRInfo) {
        this.oRInfo = oRInfo;
    }


}
