package com.plantdata.kgcloud.plantdata.bean;


import com.plantdata.kgcloud.plantdata.rsp.MarkObject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 时间统计接受参数
 *
 * @author Administrator
 */
@AllArgsConstructor
@NoArgsConstructor
public class DateTypeBean implements MarkObject {
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


}
