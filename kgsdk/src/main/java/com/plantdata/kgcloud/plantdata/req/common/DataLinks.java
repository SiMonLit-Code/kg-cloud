package com.plantdata.kgcloud.plantdata.req.common;

import lombok.Data;

import java.util.List;

/**
 * 知识卡片接口返回增加字段
 * wuyue
 * 20190601
 *
 * @author Administrator
 */
@Data
public class DataLinks {
    /**
     * 数据集ID
     */
    private Long dataSetId;
    /**
     * 数据集Title
     */
    private String dataSetTitle;
    /**
     * 数据集
     */
    private List<Links> links;
}



