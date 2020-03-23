package com.plantdata.kgcloud.domain.graph.quality.service;

import com.plantdata.kgcloud.domain.graph.quality.rsp.GraphAttrQualityRsp;
import com.plantdata.kgcloud.domain.graph.quality.rsp.GraphQualityRsp;

import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2020/3/21 14:25
 * @Description:
 */
public interface GraphQualityService {

    /**
     * 当前图谱下的概念统计列表
     *
     * @param kgName
     * @return
     */
    List<GraphQualityRsp> listConceptQuality(String kgName);


    /**
     * 当前概念子概念质量统计信息
     *
     * @param kgName
     * @param conceptId
     * @return
     */
    List<GraphQualityRsp> sonConceptCount(String kgName, Long conceptId);

    /**
     * 概念本身质量统计信息
     *
     * @param kgName
     * @param selfId
     * @return
     */
    GraphAttrQualityRsp detailByConceptId(String kgName, Long selfId);
}
