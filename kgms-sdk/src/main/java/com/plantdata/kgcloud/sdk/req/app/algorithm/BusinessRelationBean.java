package com.plantdata.kgcloud.sdk.req.app.algorithm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor
public class BusinessRelationBean {

    private String id;
    private Long from;
    private Long to;
    private Integer attId;
    private String attName;
    private List<String> startTime;
    private List<String> endTime;
    @JsonProperty("nRInfo")
    private List<RelationInfoBean> nRInfo;
    @JsonProperty("oRInfo")
    private List<RelationInfoBean> oRInfo;
    private Integer direction;
    private String creationTime;
    private Map<String, Object> origin;
    private Map<String, Object> style;
    private LinkStyleBean linkStyle;

    private LabelStyleBean labelStyle;

    public Map<String, Object> getOrigin() {
        return origin;
    }


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

    public void addrObjectInfo(RelationInfoBean one) {
        if (this.oRInfo == null) {
            this.oRInfo = new ArrayList<>();
        }
        this.oRInfo.add(one);
    }


    public BusinessRelationBean(String id, Long from, Long to, Integer attId) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.attId = attId;
    }

    public LinkStyleBean getLinkStyle() {
        return linkStyle;
    }

    public void setLinkStyle(LinkStyleBean linkStyle) {
        this.linkStyle = linkStyle;
    }

    public LabelStyleBean getLabelStyle() {
        return labelStyle;
    }

    public void setLabelStyle(LabelStyleBean labelStyle) {
        this.labelStyle = labelStyle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public Integer getAttId() {
        return attId;
    }

    public void setAttId(Integer attId) {
        this.attId = attId;
    }

    public String getAttName() {
        return attName;
    }

    public void setAttName(String attName) {
        this.attName = attName;
    }

    public List<String> getStartTime() {
        return startTime;
    }

    public void setStartTime(List<String> startTime) {
        this.startTime = startTime;
    }

    public List<String> getEndTime() {
        return endTime;
    }

    public void setEndTime(List<String> endTime) {
        this.endTime = endTime;
    }

    @JsonIgnore
    public List<RelationInfoBean> getnRInfo() {
        return nRInfo;
    }

    public void setnRInfo(List<RelationInfoBean> nRInfo) {
        this.nRInfo = nRInfo;
    }

    @JsonIgnore
    public List<RelationInfoBean> getoRInfo() {
        return oRInfo;
    }

    public void setoRInfo(List<RelationInfoBean> oRInfo) {
        this.oRInfo = oRInfo;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public void setOrigin(Map<String, Object> origin) {
        this.origin = origin;
    }

    public Map<String, Object> getStyle() {
        return style;
    }

    public void setStyle(Map<String, Object> style) {
        this.style = style;
    }


}
