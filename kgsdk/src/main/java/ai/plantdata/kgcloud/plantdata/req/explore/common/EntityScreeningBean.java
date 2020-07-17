package ai.plantdata.kgcloud.plantdata.req.explore.common;

import java.util.List;

/**
 * @author Administrator
 */
public class EntityScreeningBean {
    private Integer attrId;
    private Object $eq;
    private Object $gt;
    private Object $lt;
    private Object $gte;
    private Object $lte;
    private List<Object> $in;
    private List<Object> $nin;
    private Object $ne;
    private Integer relation;

    public Integer getAttrId() {
        return attrId;
    }

    public void setAttrId(Integer attrId) {
        this.attrId = attrId;
    }

    public Object get$eq() {
        return $eq;
    }

    public void set$eq(Object $eq) {
        this.$eq = $eq;
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

    public Object get$gte() {
        return $gte;
    }

    public void set$gte(Object $gte) {
        this.$gte = $gte;
    }

    public Object get$lte() {
        return $lte;
    }

    public void set$lte(Object $lte) {
        this.$lte = $lte;
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

    public Object get$ne() {
        return $ne;
    }

    public void set$ne(Object $ne) {
        this.$ne = $ne;
    }

    public Integer getRelation() {
        return relation;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }
}
