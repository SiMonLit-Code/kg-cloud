package com.plantdata.kgcloud.domain.common.entity;

import lombok.Data;

import java.util.List;

@Data
public class TitleLevel {

    private String text;
    private Integer level;
    private String serialNumber;

    public TitleLevel() {
    }

    public TitleLevel(String text, Integer level) {
        this.text = text;
        this.level = level;
    }

    public void setSerialNumber(List<TitleLevel> titleList){

        if(titleList == null){
            return ;
        }


        TitleLevel parent = null;
        Integer count = 1;
       for(TitleLevel titleLevel : titleList){
           if(titleLevel.getLevel() < this.level){
               parent = titleLevel;
               count=1;
           }else if(titleLevel.getLevel() == this.level){
               count++;
           }
       }

       if(parent == null){
           this.serialNumber = count+"";
           return;
       }

       String parentSerialNum = parent.getSerialNumber();
       this.serialNumber = parentSerialNum+"."+count;
    }

}
