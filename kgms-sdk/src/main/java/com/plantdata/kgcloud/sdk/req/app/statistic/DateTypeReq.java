package com.plantdata.kgcloud.sdk.req.app.statistic;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Administrator
 */
@Getter
@Setter
public class DateTypeReq {
    private String $lte;
    private String $gte;
    private Integer type;

    public String get$lte() {
        return $lte;
    }

    public void set$lte(String $lte) {
        this.$lte = $lte;
    }

    public String get$gte() {
        return $gte;
    }

    public void set$gte(String $gte) {
        this.$gte = $gte;
    }
}