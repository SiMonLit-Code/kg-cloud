package com.plantdata.kgcloud.domain.graph.quality.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class InitFunc {

    @Autowired
    private ConceptUtils conceptUtils;
    @Autowired
    private SchemaUtils schemaUtils;

    /**
     * 所有概念和名称
     */
    public static Map<Long, String> conceptNameMap;

    /**
     * 属性名称和ID
     */
    public static Map<String, Integer> attrNameMap;

    /**
     * 属性ID和名称
     */
    public static Map<Integer, String> attrIdMap;

    /**
     * 所有概念的实体数
     */
    public static Map<Long, Long> longLongMap;

    /**
     * 所有概念的父概念
     */
    public static Map<Long, Long> sonOneParentMap2;

    /**
     * 所有概念的子概念（不包含自己）
     */
    public static Map<Long, Set<Long>> sonConceptIds = new HashMap<>();

    /**
     * 所有概念的子概念（包含自己）
     */
    public static Map<Long, Set<Long>> sonAndSelfConceptIds = new HashMap<>();

    /**
     * 所有概念的父概念(已手动去除0（根节点）不包括自己）
     */
    public static Map<Long, Set<Long>> parentConceptIds = new HashMap<>();

    public void init(String kgDbname) {
        log.info("=============init初始化 开始===========");
        conceptNameMap = schemaUtils.conceptNamemap1(kgDbname);
        for (Map.Entry<Long, String> entry : conceptNameMap.entrySet()) {

            Long conceptId = entry.getKey();

            Set<Long> sonConceptId = conceptUtils.getSonConceptId(kgDbname, conceptId);
            sonConceptIds.put(conceptId, sonConceptId);

            Set<Long> sonAndSelfConceptId = new HashSet<>(sonConceptId);
            sonAndSelfConceptId.add(conceptId);
            sonAndSelfConceptIds.put(conceptId, sonAndSelfConceptId);

            Set<Long> parentConceptId = conceptUtils.getParentConceptId(kgDbname, conceptId);
            parentConceptIds.put(conceptId, parentConceptId);
        }
        longLongMap = conceptUtils.countEntityallConcepts(kgDbname);
        List<Map> list = schemaUtils.getAttrName(kgDbname);
        attrNameMap = list.get(0);
        attrIdMap = list.get(1);
        sonOneParentMap2 = schemaUtils.sonOneParentMap2(kgDbname);
        log.info("=============init初始化 结束===========");
    }

}
