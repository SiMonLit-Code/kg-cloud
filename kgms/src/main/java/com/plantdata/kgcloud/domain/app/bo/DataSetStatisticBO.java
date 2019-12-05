package com.plantdata.kgcloud.domain.app.bo;

import com.plantdata.kgcloud.domain.app.dto.AggsDTO;
import com.plantdata.kgcloud.domain.app.dto.EsDTO;
import com.plantdata.kgcloud.sdk.constant.DataSetStatisticEnum;
import com.plantdata.kgcloud.sdk.constant.DimensionEnum;
import com.plantdata.kgcloud.sdk.req.app.DataSetStatisticRsp;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.boot.configurationprocessor.json.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/7 18:14
 */
@ToString
public class DataSetStatisticBO {
    @Getter
    private AggsDTO aggsDTO;
    @Getter
    private EsDTO esDTO;
    @Getter
    private DataSetStatisticEnum setStatisticEnum;
    private DimensionEnum dimension;

    /**
     * 初始化模型
     *
     * @param aggs       ...
     * @param query      查询条件
     * @param returnType 显示类型
     * @return ...
     */
    public DataSetStatisticBO init(String aggs, String query, DimensionEnum dimension, int returnType) {
        this.aggsDTO = AggsDTO.factory(aggs);
        this.esDTO = new EsDTO(aggs, NumberUtils.INTEGER_ZERO, query != null && !StringUtils.EMPTY.equals(query) ? query : null);
        this.setStatisticEnum = DataSetStatisticEnum.parseByValue(returnType);
        this.dimension = dimension;
        return this;
    }

    /**
     * 后置数据处理
     *
     * @param data es数据
     * @return 。。
     */
    public DataSetStatisticRsp postDealData(List<Map<String, Object>> data) {
        return dimension.equals(DimensionEnum.TWO)
                ? postDataDealByReturnType(data)
                : postDataDealNoReturnType(data);
    }

    private DataSetStatisticRsp postDataDealByReturnType(List<Map<String, Object>> data) {
        JSONArray arr = this.buildAttrArray(data);
        DataSetStatisticRsp rsp = new DataSetStatisticRsp();
        //  List<String> xData = arr == null ? Collections.emptyList() : new ArrayList<>(arr.size());
//        List<Double> sData = arr == null ? Collections.emptyList() : new ArrayList<>(arr.size());
//        if (Objects.nonNull(arr)) {
//            for (int i = 0; i < arr.size(); i++) {
//                JSONObject obj = arr.getJSONObject(i);
//                String k = obj.getString("key_as_string");
//                k = Objects.isNull(k) ? obj.getString("key") : k;
//                long v = obj.getLong("doc_count");
//                if (DataSetStatisticEnum.KV.equals(setStatisticEnum)) {
//                    rsp.addKVData2Series(k, v);
//                } else {
//                    xData.add(k);
//                    sData.add((double) v);
//                }
//            }
//        }
//        rsp.addData2X(xData);
//        rsp.addData2Series(StringUtils.EMPTY, sData);
        return rsp;
    }

    private DataSetStatisticRsp postDataDealNoReturnType(List<Map<String, Object>> data) {
        JSONArray arr = this.buildAttrArray(data);
        DataSetStatisticRsp rsp = new DataSetStatisticRsp();
//        Table<String, String, Double> rsTable = HashBasedTable.create();
//        if (Objects.nonNull(arr)) {
//            for (int i = 0; i < arr.size(); i++) {
//                JSONObject obj1 = arr.getJSONObject(i);
//                String k1 = obj1.getString("key_as_string");
//                k1 = Objects.isNull(k1) ? obj1.getString("key") : k1;
//                JSONObject byKey2 = obj1.getJSONObject("by_key2");
//                JSONArray arr2 = byKey2.getJSONArray("buckets");
//                for (int j = 0; j < arr2.size(); j++) {
//                    JSONObject obj2 = arr2.getJSONObject(j);
//                    String k2 = obj2.getString("key_as_string");
//                    k2 = Objects.isNull(k2) ? obj2.getString("key") : k2;
//                    long v = obj2.getLong("doc_count");
//                    rsTable.put(k1, k2, (double) v);
//                }
//            }
//        }
//
//        List<String> xData = new ArrayList<>(rsTable.rowKeySet());
//        rsp.addData2X(xData);
//
//        for (String k2 : rsTable.columnKeySet()) {
//            List<Double> sData = new ArrayList<>();
//            for (String k1 : xData) {
//                Double v = rsTable.get(k1, k2);
//                v = Objects.isNull(v) ? 0.0 : v;
//                sData.add(v);
//            }
//            rsp.addData2Series(k2, sData);
//    }
        return rsp;
    }

    private JSONArray buildAttrArray(List<Map<String, Object>> data) {
//        JSONObject jsonObj = JSON.parseObject(JSON.toJSONString(data));
//        JSONObject byKey = jsonObj.getJSONObject("aggregations").getJSONObject("by_key1");
//        return aggsDTO.getIsNested() ? byKey.getJSONObject(aggsDTO.getNestedName()).getJSONArray("buckets") : byKey.getJSONArray("buckets");
        return null;
    }

}
