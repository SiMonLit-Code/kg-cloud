package com.plantdata.kgcloud.plantdata.req.explore;


import com.plantdata.kgcloud.plantdata.link.LinkModel;
import com.plantdata.kgcloud.sdk.req.app.RelationAttrReq;

import java.util.List;
/**
 * @author Administrator
 */
@LinkModel(clazz = RelationAttrReq.class)
public class AttrScreeningBean{
    private Integer attrId;
    private Integer seqNo;
    private String $eq;
    private String $neq;
    private Object $gt;
    private Object $lt;
    private Object $lte;
    private Object $gte;
    private List<Object> $in;
    private List<Object> $nin;

    public Integer getAttrId() {
        return attrId;
    }

    public void setAttrId(Integer attrId) {
        this.attrId = attrId;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public String get$eq() {
        return $eq;
    }

    public void set$eq(String $eq) {
        this.$eq = $eq;
    }

    public String get$neq() {
        return $neq;
    }

    public void set$neq(String $neq) {
        this.$neq = $neq;
    }

    public Object get$gt() {
        return $gt;
    }

    public void set$gt(Object $gt) {
        this.$gt = $gt;
    }

    public Object get$lt() {
        return $lt;
    }

    public void set$lt(Object $lt) {
        this.$lt = $lt;
    }

    public Object get$lte() {
        return $lte;
    }

    public void set$lte(Object $lte) {
        this.$lte = $lte;
    }

    public Object get$gte() {
        return $gte;
    }

    public void set$gte(Object $gte) {
        this.$gte = $gte;
    }

    public List<Object> get$in() {
        return $in;
    }

    public void set$in(List<Object> $in) {
        this.$in = $in;
    }

    public List<Object> get$nin() {
        return $nin;
    }

    public void set$nin(List<Object> $nin) {
        this.$nin = $nin;
    }
}
