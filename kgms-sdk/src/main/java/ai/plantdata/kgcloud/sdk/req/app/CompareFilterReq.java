package ai.plantdata.kgcloud.sdk.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/12 17:11
 */
@Getter
@Setter
@ApiModel("大小比较参数")
@NoArgsConstructor
public class CompareFilterReq {

    @ApiModelProperty("大于")
    private Object $gt;
    @ApiModelProperty("大于等于")
    private Object $gte;
    @ApiModelProperty("小于")
    private Object $lt;
    @ApiModelProperty("小于等于")
    private Object $lte;
    @ApiModelProperty("等于")
    private Object $eq;
    @ApiModelProperty("包含")
    private Object $in;
    @ApiModelProperty("不等于")
    private Object $ne;
    @ApiModelProperty("不等于")
    private Object $nin;

    public Object get$gt() {
        return $gt;
    }

    public void set$gt(Object $gt) {
        this.$gt = $gt;
    }

    public Object get$gte() {
        return $gte;
    }

    public void set$gte(Object $gte) {
        this.$gte = $gte;
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

    public Object get$eq() {
        return $eq;
    }

    public void set$eq(Object $eq) {
        this.$eq = $eq;
    }

    public Object get$in() {
        return $in;
    }

    public void set$in(Object $in) {
        this.$in = $in;
    }

    public Object get$ne() {
        return $ne;
    }

    public void set$ne(Object $ne) {
        this.$ne = $ne;
    }

    public Object get$nin() {
        return $nin;
    }

    public void set$nin(Object $nin) {
        this.$nin = $nin;
    }
}
