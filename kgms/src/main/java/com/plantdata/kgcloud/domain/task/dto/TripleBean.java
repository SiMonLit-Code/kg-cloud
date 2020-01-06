package com.plantdata.kgcloud.domain.task.dto;

public class TripleBean {

    private String id;

    private Integer status;

    private NodeBean start;
    private EdgeBean edge;
    private NodeBean end;

    private String reason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public NodeBean getStart() {
        return start;
    }

    public void setStart(NodeBean start) {
        this.start = start;
    }

    public EdgeBean getEdge() {
        return edge;
    }

    public void setEdge(EdgeBean edge) {
        this.edge = edge;
    }

    public NodeBean getEnd() {
        return end;
    }

    public void setEnd(NodeBean end) {
        this.end = end;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
