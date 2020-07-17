package ai.plantdata.kgcloud.sdk.req.app.statistic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Administrator
 */
@Getter
@Setter
@ApiModel("日期类型参数")
public class DateTypeReq {
    @ApiModelProperty("小于等于")
    private String $lte;
    @ApiModelProperty("大于等于")
    private String $gte;
    @ApiModelProperty("1 按年显示 2按季度显示 3按月显示 4精确到天 5精确到小时 6精确到秒")
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