package ai.plantdata.kgcloud.domain.graph.quality.util;

import ai.plantdata.kg.common.bean.BasicInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Data
public class InitFunc {

    /**
     * 所有概念信息
     */
    private final Map<Long, BasicInfo> conceptMap = new HashMap<>();

    /**
     * 属性ID和名称
     */
    private final Map<Integer, String> attrIdMap = new HashMap<>();

    /**
     * 属性名称和ID
     */
    private final Map<String, Integer> attrNameMap = new HashMap<>();

    /**
     * 所有概念的子概念（不包含自己）
     */
    private Map<Long, Set<Long>> sonConceptIdMap = new HashMap<>();

    /**
     * 所有概念的父概念(已手动去除0（根节点）不包括自己）
     */
    private Map<Long, Set<Long>> parentConceptIdMap = new HashMap<>();

}
