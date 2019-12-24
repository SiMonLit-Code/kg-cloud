package com.plantdata.kgcloud.plantdata.req.explore.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphStatBean {

    private String key;
    private Long type;
    private List<Integer> atts;
    private List<GraphStatDetailBean> rs;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GraphStatDetailBean {
        Long id;
        Integer count;
    }
}
