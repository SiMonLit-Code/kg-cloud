package com.plantdata.kgcloud.domain.app.bo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.plantdata.kgcloud.domain.app.dto.AggsDTO;
import com.plantdata.kgcloud.domain.app.dto.EsDTO;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.sdk.constant.DataSetStatisticEnum;
import com.plantdata.kgcloud.sdk.constant.DimensionEnum;
import com.plantdata.kgcloud.sdk.req.app.DataSetStatisticRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        ArrayNode arr = this.buildAttrArray(data);
        DataSetStatisticRsp rsp = new DataSetStatisticRsp();
        List<String> xData = arr == null ? Collections.emptyList() : new ArrayList<>(arr.size());
        List<Double> sData = arr == null ? Collections.emptyList() : new ArrayList<>(arr.size());
        if (Objects.nonNull(arr)) {
            for (int i = 0; i < arr.size(); i++) {
                JsonNode obj = arr.get(i);
                String k = obj.get("key_as_string").toString();
                k = Objects.isNull(k) ? obj.get("key").toString() : k;
                long v = Long.parseLong(obj.get("doc_count").toString());
                if (DataSetStatisticEnum.KV.equals(setStatisticEnum)) {
                    rsp.addData2Series(k, v);
                } else {
                    xData.add(k);
                    sData.add((double) v);
                }
            }
        }
        rsp.addData2xAxis(xData);
        rsp.addData2Series(StringUtils.EMPTY, sData);
        return null;
    }

    private DataSetStatisticRsp postDataDealNoReturnType(List<Map<String, Object>> data) {
        ArrayNode arr = this.buildAttrArray(data);
        DataSetStatisticRsp rsp = new DataSetStatisticRsp();
        Table<String, String, Double> rsTable = HashBasedTable.create();
        if (Objects.nonNull(arr)) {
            for (int i = 0; i < arr.size(); i++) {
                JsonNode obj1 = arr.get(i);
                String k1 = obj1.get("key_as_string").toString();
                k1 = Objects.isNull(k1) ? obj1.get("key").toString() : k1;
                JsonNode byKey2 = obj1.get("by_key2");
                ArrayNode arr2 = (ArrayNode) byKey2.get("buckets");
                for (int j = 0; j < arr2.size(); j++) {
                    JsonNode obj2 = arr2.get(j);
                    String k2 = obj2.get("key_as_string").toString();
                    k2 = Objects.isNull(k2) ? obj2.get("key").toString() : k2;
                    long v = Long.parseLong(obj2.get("doc_count").toString());
                    rsTable.put(k1, k2, (double) v);
                }
            }
        }

        List<String> xData = new ArrayList<>(rsTable.rowKeySet());
        rsp.addData2xAxis(xData);

        for (String k2 : rsTable.columnKeySet()) {
            List<Double> sData = new ArrayList<>();
            for (String k1 : xData) {
                Double v = rsTable.get(k1, k2);
                v = Objects.isNull(v) ? 0.0 : v;
                sData.add(v);
            }
            rsp.addData2Series(k2, sData);
        }
        return rsp;
    }

    private ArrayNode buildAttrArray(List<Map<String, Object>> data) {
        JsonNode jsonObj = JsonNodeFactory.instance.pojoNode(data);
        JsonNode byKey = jsonObj.get("aggregations").get("by_key1");
        JsonNode arrayNode = aggsDTO.getIsNested() ? byKey.get(aggsDTO.getNestedName()).get("buckets") : byKey.get("buckets");
        return (ArrayNode) arrayNode;
    }

}
