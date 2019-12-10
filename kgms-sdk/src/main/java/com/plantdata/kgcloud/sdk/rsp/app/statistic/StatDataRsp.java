package com.plantdata.kgcloud.sdk.rsp.app.statistic;

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
    public void addData2Series(String name, List data) {
        Map<String, Object> m = new HashMap<>(2);
        m.put("name", name);
        m.put("data", data);
        this.series.add(m);
    }

    public void addKVData2Series(String k, Double v) {
        Map<String, Object> m = new HashMap<>(2);
        m.put("name", k);
        m.put("value", v);
        this.series.add(m);
    }

    public void addKVData2Series(String k, Long v) {
        Map<String, Object> m = new HashMap<>(2);
        m.put("name", k);
        m.put("value", v);
        this.series.add(m);
    }
}
