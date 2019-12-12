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
    private String $gt;
    @ApiModelProperty("小于")
    private String $lt;
    @ApiModelProperty("等于")
    private String $eq;

    public String get$gt() {
        return $gt;
    }

    public void set$gt(String $gt) {
        this.$gt = $gt;
    }

    public String get$lt() {
        return $lt;
    }

    public void set$lt(String $lt) {
        this.$lt = $lt;
    }

    public String get$eq() {
        return $eq;
    }

    public void set$eq(String $eq) {
        this.$eq = $eq;
    }
}
