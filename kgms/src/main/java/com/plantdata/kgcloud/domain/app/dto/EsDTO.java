package com.plantdata.kgcloud.domain.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/5 13:02
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EsDTO {
    @JsonProperty("_source")
    private List<String> source;
    private Object query;
    private Integer from;
    private Integer size;
    private Object sort;
    private String aggs;

    public EsDTO(Object query, int size, String aggs) {
        this.query = query;
        this.size = size;
        this.aggs = aggs;
        this.from = 0;
    }

    public Map<String, Object> parseMap() {
        String s = JsonUtils.toJson(this);
        return JacksonUtils.readValue(s, new TypeReference<Map<String, Object>>() {
        });
    }
}
