package com.plantdata.kgcloud.sdk.req;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-09 21:53
 **/
@Data
public class AnnotationConf {
    private String key;
    private String title;
    private Integer source;
    private List<Long> classId;
    private Long classIdSelected;
}
