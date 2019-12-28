package com.plantdata.kgcloud.domain.annotation.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author xiezhenxiang 2019/6/4
 */
@Data
@ApiModel
public class SettingReq {

    public String kgName;
    /** 标引算法 2：语义相似算法 */
    public List<Integer> algorithms;
    /** 数据集ID */
    public Long dataSetId;
    /** 标引范围 */
    public List<TargetConcept> targetConcepts;
    /** 关联概念 */
    public Set<TranceBean> traceConfig;
    /** 权重配置 <字段, 权重> */
    public HashMap<String, Double> fieldsAndWeights;
}
