package com.plantdata.kgcloud.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/29 14:03
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PromptResultTypeEnum {
    /**
     *
     */
    ENTITY(1,"entity"),
    CONCEPT(0,"concept"),
    CONCEPT_ENTITY(10,"entity_concept");
    int id;
    String desc;

    public  static PromptResultTypeEnum parseWithDefault(String type){
        for(PromptResultTypeEnum typeEnum:PromptResultTypeEnum.values()){
            if(typeEnum.desc.equals(type)){
                return typeEnum;
            }
        }
        return CONCEPT_ENTITY;
    }
}
