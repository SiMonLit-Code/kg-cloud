package com.plantdata.kgcloud.sdk.rsp.app.nlp;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/6 18:31
 */
@Getter
@Setter
public class TaggingItemRsp {

    private Long id;
    private String name;
    private Double score;
    private Long classId;

}
