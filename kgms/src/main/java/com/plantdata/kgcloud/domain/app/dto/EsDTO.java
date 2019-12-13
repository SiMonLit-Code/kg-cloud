package com.plantdata.kgcloud.domain.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

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
}
