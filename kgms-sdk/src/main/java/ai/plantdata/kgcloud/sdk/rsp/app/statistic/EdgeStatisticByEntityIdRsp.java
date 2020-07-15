package ai.plantdata.kgcloud.sdk.rsp.app.statistic;

import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/18 14:39
 */
@Getter
@Setter
public class EdgeStatisticByEntityIdRsp {
    private int degree;
    private int inDegree;
    private int outDegree;
    private int layer;

    public EdgeStatisticByEntityIdRsp(int layer) {
        this.layer = layer;
    }
}
