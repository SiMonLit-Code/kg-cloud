package com.plantdata.kgcloud.plantdata.req.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author xiaohuqi@126.com 2018/8/10
 * 为导入而生的实体bean
 **/
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ImportEntityBean {
    /**
     * 实体ID，建议更新导入时使用ID操作，可大大提升效率
     */
    private Long id;
    /**
     * 实体所属概念ID
     */
    private Long conceptId;
    /**
     * 实体所属多概念IDList
     */
    private List<Long> conceptIdList;
    /**
     * 实体名称
     */
    private String name;
    /**
     * 消歧标识
     */
    private String meaningTag;
    /**
     * 同义列表
     */
    private List<String> synonyms;
    /**
     * 摘要
     */
    private String abs;
    /**
     * 图片url
     */
    private String imageUrl;
    /**
     * 属性列表
     */
    private Map<String, Object> attributes;
    /**
     * 属性列表
     */
    private Map<String, String> privateAttributes;
    private String note;

    private Map<String,Object>metaData;
}
