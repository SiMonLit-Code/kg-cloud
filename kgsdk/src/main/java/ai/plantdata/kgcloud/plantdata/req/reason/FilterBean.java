package ai.plantdata.kgcloud.plantdata.req.reason;


import ai.plantdata.kgcloud.sdk.constant.AggregateEnum;
import ai.plantdata.kgcloud.sdk.constant.SignEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Administrator
 */
@Getter
@Setter
public class FilterBean {
    private AggregateEnum agg;
    private String attrId;
    private SignEnum sign;
    private Object value;
    private SecEnum sec;
    private String unit;
}
