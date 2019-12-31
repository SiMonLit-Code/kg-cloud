package com.plantdata.kgcloud.sdk.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/12 17:11
 */
@Getter
@Setter
@ApiModel("大小比较参数")
public class CompareFilterReq {

    @ApiModelProperty("大于")
    private Object $gt;
    @ApiModelProperty("大于等于")
    private String $gte;
    @ApiModelProperty("小于")
    private Object $lt;
    @ApiModelProperty("小于等于")
    private Object $lte;
    @ApiModelProperty("等于")
    private String $eq;
    @ApiModelProperty("包含")
    private Object $in;
    @ApiModelProperty("不等于")
    private Object $neq;
    @ApiModelProperty("不等于")
    private Object $nin;

    public Object get$nin() {
        return $nin;
    }

    public void set$nin(Object $nin) {
        this.$nin = $nin;
    }

    public Object get$neq() {
        return $neq;
    }

    public void set$neq(Object $neq) {
        this.$neq = $neq;
    }

    public Object get$lte() {
        return $lte;
    }

    public void set$lte(Object $lte) {
        this.$lte = $lte;
    }

    public Object get$in() {
        return $in;
    }

    public void set$in(Object $in) {
        this.$in = $in;
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

    public String get$eq() {
        return $eq;
    }

    public void set$eq(String $eq) {
        this.$eq = $eq;
    }

    public String get$gte() {
        return $gte;
    }

    public void set$gte(String $gte) {
        this.$gte = $gte;
    }
}
