package com.plantdata.kgcloud.domain.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.plantdata.kgcloud.sdk.req.app.infobox.InfoBoxReq;
import com.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import com.plantdata.kgcloud.sdk.constant.GraphInitBaseEnum;
import com.plantdata.kgcloud.sdk.req.app.GraphInitRsp;
import com.plantdata.kgcloud.sdk.req.app.infobox.BatchInfoBoxReq;
import com.plantdata.kgcloud.sdk.rsp.app.main.BasicConceptTreeRsp;
import com.plantdata.kgcloud.sdk.req.app.KnowledgeRecommendReq;
import com.plantdata.kgcloud.sdk.req.app.ObjectAttributeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 14:37
 */
public interface GraphApplicationService {

    /**
     * 查询概念和属性
     *
     * @param kgName 图谱名称
     * @return schema
     */
    SchemaRsp querySchema(String kgName);

    /**
     * 知识推荐
     *
     * @param kgName                图谱名称
     * @param knowledgeRecommendReq 过滤参数
     * @return result
     */
    List<ObjectAttributeRsp> knowledgeRecommend(String kgName, KnowledgeRecommendReq knowledgeRecommendReq);

    /**
     * 获取可视化模型数据
     *
     * @param kgName    图谱名称
     * @param display   是否显示对象属性
     * @param conceptId 概念id
     * @return result
     */
    BasicConceptTreeRsp visualModels(String kgName, boolean display, Long conceptId);

    /**
     * 初始化图探索
     *
     * @param kgName        图谱名称
     * @param graphInitType 图谱初始化类型
     * @return obj
     */
    GraphInitRsp initGraphExploration(String kgName, GraphInitBaseEnum graphInitType) throws JsonProcessingException;

    /**
     * conceptId  conceptKey不能同时为null
     *
     * @param kgName     图谱名称
     * @param conceptId  概念id
     * @param conceptKey 概念key
     * @return list
     */
    List<BasicInfoVO> conceptTree(String kgName, Long conceptId, String conceptKey);

    /**
     * 单个查询知识卡片 包含标引数据集
     *
     * @param kgName
     * @param userId
     * @param infoBoxReq
     * @return
     */
    InfoBoxRsp infoBox(String kgName, String userId, InfoBoxReq infoBoxReq);

    /**
     * 知识卡片
     *
     * @param kgName 图谱名称
     * @param req    参数
     * @return obj
     */
    List<InfoBoxRsp> infoBox(String kgName, BatchInfoBoxReq req);
}
