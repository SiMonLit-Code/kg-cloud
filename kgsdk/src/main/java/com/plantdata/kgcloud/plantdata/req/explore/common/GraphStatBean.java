package com.plantdata.kgcloud.plantdata.req.explore.common;

import com.plantdata.kgcloud.plantdata.rsp.MarkObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GraphStatBean implements MarkObject {

    private String key;
    private Long type;
    private List<Integer> atts;
    private List<GraphStatDetailBean> rs;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GraphStatDetailBean {
        Long id;
        Integer count;
    }
}
