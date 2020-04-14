package com.plantdata.kgcloud.domain.chanyelian.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author xiezhenxiang 2020/4/13
 */
@Data
public class Node {

    private Long id;
    private String name;
    private String edgeName;
    private List<Node> next;
    private List<Node> last;
    private Map<String, Object> attr;

    public Node(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Node(Long id, String name, String edgeName) {
        this.id = id;
        this.name = name;
        this.edgeName = edgeName;
    }
}
