package com.plantdata.kgcloud;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/14 16:52
 */
public class MapTest {

    public static void main(String[] args) {
        List<Obj> list = new ArrayList<>();
        list.add(new Obj("a"));
        list.add(new Obj("b"));
        Map<String, Obj> map = new HashMap<>();
        list.forEach(a -> map.computeIfAbsent(a.getName(),v->new Obj(a.getName())));
        System.out.println(JSON.toJSONString(map));
    }
}


class Obj {
    private String name;

    public String getName() {
        return name;
    }

    public Obj(String name) {
        this.name = name;
    }
}