package com.plantdata.kgcloud.domain.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.plantdata.kgcloud.sdk.constant.GraphInitBaseEnum;
import com.plantdata.kgcloud.sdk.req.app.ComplexGraphVisualReq;
import com.plantdata.kgcloud.sdk.req.app.GraphInitRsp;
import com.plantdata.kgcloud.sdk.req.app.KnowledgeRecommendReq;
import com.plantdata.kgcloud.sdk.req.app.ObjectAttributeRsp;
import com.plantdata.kgcloud.sdk.req.app.dataset.PageReq;
import com.plantdata.kgcloud.sdk.req.app.infobox.BatchInfoBoxReq;
import com.plantdata.kgcloud.sdk.req.app.infobox.InfoBoxReq;
import com.plantdata.kgcloud.sdk.rsp.app.ComplexGraphVisualRsp;
import com.plantdata.kgcloud.sdk.rsp.app.PageRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.ApkRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.BasicConceptTreeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;

import javax.validation.Valid;
import java.io.IOException;
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
    List<ObjectAttributeRsp> knowledgeRecommend(String kgName, @Valid KnowledgeRecommendReq knowledgeRecommendReq);

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
     * 分页查询所有图谱
     *
     * @param pageReq 分页参数
     * @return 。
     */
    PageRsp<ApkRsp> listAllGraph(PageReq pageReq);

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
     * @param kgName     图谱名称
     * @param userId     id
     * @param infoBoxReq req
     * @return obj
     */
    InfoBoxRsp infoBox(String kgName, String userId, InfoBoxReq infoBoxReq) throws IOException;

    /**
     * 知识卡片
     *
     * @param kgName 图谱名称
     * @param req    参数
     * @return list
     */
    List<InfoBoxRsp> infoBox(String kgName, BatchInfoBoxReq req);

    /**
     * 复杂图算法 可视化展示
     *
     * @param kgName      图谱名称
     * @param analysisReq req
     * @return .
     */
    ComplexGraphVisualRsp complexGraphVisual(String kgName, ComplexGraphVisualReq analysisReq);
}
