package com.plantdata.kgcloud.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/29 14:47
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum  PromptQaTypeEnum {
    /**
     * 0 不含问答结果 1问答结果在前 2问答结果在后边
     */
    DEFAULT(0),
    BEFORE(1),
    END(2);
    int id;

    public  static PromptQaTypeEnum parseWitDefault(int id){
        for (PromptQaTypeEnum typeEnum:PromptQaTypeEnum.values()){
            if(typeEnum.id==id){
                return typeEnum;
            }
        }
        return DEFAULT;
    }
}
