package ai.plantdata.kgcloud.sdk.rsp.app.statistic;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Getter
@Setter
public class StatDataRsp {

    public class keyType {
        public final static String Term = "term";
        public final static String Date = "date";
    }

    private List<Object> xAxis = new ArrayList<>();
    private List<Object> series = new ArrayList<>();


    @SuppressWarnings("rawtypes")
    public void addData2X(List data) {
        Map<String, Object> m = new HashMap<>(1);
        m.put("data", data);
        this.xAxis.add(m);
    }

    @SuppressWarnings("rawtypes")
    public void addData2Series(List data) {
        Map<String, Object> m = new HashMap<>(2);
        m.put("data", data);
        this.series.add(m);
    }
}
