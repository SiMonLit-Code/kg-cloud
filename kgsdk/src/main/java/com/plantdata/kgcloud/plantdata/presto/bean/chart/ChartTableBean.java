package com.plantdata.kgcloud.plantdata.presto.bean.chart;

import com.google.common.collect.Lists;

import java.util.List;

public class ChartTableBean {

    private List<String> name = Lists.newArrayList();

    private List<List<Object>> data = Lists.newArrayList();

    public void setName(List<String> name) {
        this.name = name;
    }

    public void setData(List<List<Object>> data) {
        this.data = data;
    }

    public List<String> getName() {
        return name;
    }

    public List<List<Object>> getData() {
        return data;
    }

    public void addName(String name){
        this.name.add(name);
    }

    public void addData(List<Object> data){
        this.data.add(data);
    }

    public void print(){

        for (String s : name) {
            System.out.print(s + " ");
        }

        System.out.println();

        for (List<Object> datum : data) {
            for (Object o : datum) {
                System.out.print(o + " ");
            }
            System.out.println();
        }


    }
}
