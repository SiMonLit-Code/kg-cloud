package com.plantdata.kgcloud.domain.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/11 10:48
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class StatisticDTO {
    private String name;
    private Long total;
    private Long id;
    private String value;
    private List<Long> entity;
    private List<String> relation;

    public StatisticDTO(StatisticDTO resultsBean) {
        this.name = resultsBean.getName();
        this.total = resultsBean.getTotal();
        this.id = resultsBean.getId();
        this.value = resultsBean.getValue();
        this.entity = resultsBean.getEntity();
        this.relation = resultsBean.getRelation();
    }
}
