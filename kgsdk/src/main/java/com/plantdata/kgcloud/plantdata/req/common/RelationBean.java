package com.plantdata.kgcloud.plantdata.req.common;

import com.plantdata.kgcloud.plantdata.link.LinkModel;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GraphRelationRsp;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@LinkModel(clazz = GraphRelationRsp.class)
public class RelationBean extends RelationMetaData {

    private String id;
    private Long from;
    private Long to;
    private Integer attId;
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

    public List<RelationInfoBean> getnRInfo() {
        return nRInfo;
    }

    public List<RelationInfoBean> getoRInfo() {
        return oRInfo;
    }

    public void addrNumInfo(RelationInfoBean one) {
        if (this.nRInfo == null) {
            this.nRInfo = new ArrayList<>();
        }
        this.nRInfo.add(one);
    }

    public void addrObjectInfo(RelationInfoBean one) {
        if (this.oRInfo == null) {
            this.oRInfo = new ArrayList<>();
        }
        this.oRInfo.add(one);
    }

    public RelationBean(String id, Long from, Long to, Integer attId) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.attId = attId;
    }


}
