package com.plantdata.kgcloud.plantdata.req.explore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphStatBean {

    private String key;
    private Long type;
    private List<Integer> atts;
    private List<GraphStatDetailBean> rs;


    public void addRs(Long id, Integer count) {
        if (this.rs == null) {
            rs = new ArrayList<>();
        }
        rs.add(new GraphStatDetailBean(id, count));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class GraphStatDetailBean {

        Long id;
        Integer count;


    }
}
