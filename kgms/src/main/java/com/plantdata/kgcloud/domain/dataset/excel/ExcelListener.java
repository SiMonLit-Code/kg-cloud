package com.plantdata.kgcloud.domain.dataset.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelListener extends AnalysisEventListener<Map<Integer, Object>> {

    private List<Map<Integer, Object>> data = new ArrayList<>();
    private Map<Integer, String> head;

    public List<Map<Integer, Object>> getData() {
        return data;
    }

    public Map<Integer, String> getHead() {
        return head;
    }


    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        head = headMap;
    }

    @Override
    public void invoke(Map<Integer, Object> o, AnalysisContext analysisContext) {
        data.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
