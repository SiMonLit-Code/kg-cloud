package com.plantdata.kgcloud.sdk.kgcompute.graph;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

public class Dijkstras {

    public static void main(String[] args) {
        Graph g = new Graph();
        g.addVertex("交易记录", Arrays.asList(new Vertex("关联表")));
        g.addVertex("数据中心", Arrays.asList(new Vertex("关联表"),new Vertex("地区")));
        g.addVertex("设备", Arrays.asList(new Vertex("关联表")));
        g.addVertex("会员", Arrays.asList(new Vertex("关联表")));
        g.addVertex("地区", Arrays.asList(new Vertex("数据中心")));
        g.addVertex("关联表", Arrays.asList(new Vertex("交易记录"),new Vertex("数据中心"),new Vertex("设备"),new Vertex("会员")));

        System.out.println(g.getShortestPath("地区", "交易记录"));

        List<String> target_nodes = Lists.newArrayList("地区", "交易记录");

        for(int i=0; i<target_nodes.size()-1; i++) {

            String start = String.valueOf(target_nodes.get(i));
            String end = String.valueOf(target_nodes.get(i + 1));

            List<String> paths = g.getShortestPath(start, end);

            System.out.println(paths);
        }
    }

}




