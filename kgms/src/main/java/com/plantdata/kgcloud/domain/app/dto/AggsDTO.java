package com.plantdata.kgcloud.domain.app.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/7 16:52
 */
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class AggsDTO {

    private static final String BY_KEY_ONE = "by_key1";
    private static final String NESTED = "nested";

    private Boolean isNested;
    private String nestedName;
    private String json;

    public static AggsDTO factory(String aggJson) {
        JsonNode aggsJson = JacksonUtils.readValue(aggJson, JsonNode.class);
        if (aggsJson.get(BY_KEY_ONE) == null) {
            throw new IllegalArgumentException("参数错误");
        }
        if (aggsJson.get(BY_KEY_ONE).has(NESTED)) {
            return new AggsDTO(true, aggsJson.get("BY_KEY_ONE").get("aggs").fieldNames().next(), aggJson);
        }
        return new AggsDTO(false, null, aggJson);
    }
}
